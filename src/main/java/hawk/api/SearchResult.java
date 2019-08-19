package hawk.api;

import hawk.entity.Item;

import java.util.List;

public class SearchResult {

    private final String searchText;
    private final List<Item> items;

    public SearchResult(String searchText, List<Item> items) {
        this.searchText = searchText;
        this.items = items;
    }

    public String getSearchText() {
        return searchText;
    }

    public List<Item> getItems() {
        return items;
    }
}
