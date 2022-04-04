package hawk.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
public class PayloadController {

    private static final char[] chars = new char[]{'a','b','c',' ','\n'};
    private static Map<Integer, byte[]> payloadCache = new ConcurrentHashMap();

    @Value("${payload.startSize:2048}")
    private int startPayloadSize = 2048;

    @Value("${payload.count:10}")
    private int payloadCount = 20;

    @GetMapping(value={"/payload/{size}","/admin/payload/{size}"})
    public String getPayload(Model model,
                             @PathVariable("size") Integer size) {

        if(payloadCache.containsKey(size)){
            model.addAttribute("payload", new String( payloadCache.get(size)));
        }else {
            String tmpData = "mobile: 555-678-5343 ";
            String payload = tmpData + RandomStringUtils.random(size - tmpData.length(), 0, 5, false, false, chars);
            payloadCache.put(size, payload.getBytes());
            model.addAttribute("payload", payload);
        }
        return "payload-view";
    }

    @GetMapping(value={"/payloads", "/admin/payloads"})
    public String getPayloadsList(Model model){
        Integer[] payloadSizes = new Integer[payloadCount];

        for(int i = 0; i < payloadCount; i++){
            payloadSizes[i] = startPayloadSize + i;
        }

        model.addAttribute("payloads", payloadSizes);
        return "payloads";
    }

    @GetMapping(value={"/payload/stream/{size}", "/admin/payload/stream/{size}"})
    public StreamingResponseBody getPayloadStream(@PathVariable("size") Integer size) {
        String tmpData = "mobile: 555-678-5343 ";

        return new StreamingResponseBody() {
            Integer cnt = 0;
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {

                if(payloadCache.containsKey(size)){
                    IOUtils.copy(new ByteArrayInputStream(payloadCache.get(size)), outputStream);
                }else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    cnt += tmpData.length();
                    outputStream.write(tmpData.getBytes());
                    outputStream.flush();
                    while (cnt < size) {
                        int rnSize = 1024;
                        if ((cnt + rnSize) > size) {
                            rnSize = size - cnt;
                        }
                        String nl = System.lineSeparator();
                        String tp = RandomStringUtils.random(rnSize, 0, 5, false, false, chars);
                        byte[] data = (tp + nl).getBytes();
                        outputStream.write(data);
                        outputStream.flush();
                        baos.write(data);
                        cnt += data.length;
                    }
                    payloadCache.put(size, baos.toByteArray());
                }
            }
        };
    }

}
