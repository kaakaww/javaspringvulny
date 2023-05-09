import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class Comment {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String query = req.getParameter("query");

        MongoClient mongoClient = new MongoClient();
        DB database             = mongoClient.getDB("db");
        DBCollection collection = database.getCollection("coll");
        BasicDBObject query     = new BasicDBObject();

        // ok: nosql-injection-servlets
        query.put("test", input);
    }

    protected void doGet2(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String input = req.getParameter("query");

        MongoClient mongoClient = new MongoClient();
        DB database             = mongoClient.getDB("db");
        DBCollection collection = database.getCollection("coll");
        BasicDBObject query     = new BasicDBObject();

        // ruleid: nosql-injection-servlets
        query.put("$where", "this.test == \"" + input + "\""); // Noncompliant
    }

    protected void doGet3(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String input = req.getParameter("query");

        MongoClient mongoClient = new MongoClient();
        DB db = mongoClient.getDB("test");
        DBCollection coll = db.getCollection("testCollection");    
        // ruleid: nosql-injection-servlets
        BasicDBObject query = new BasicDBObject("_id", "this.field1 == \"" + input + "\"");
        DBCursor cursor = coll.find(query);
    }
}
