public class HttpPath {

    private String path;

    public HttpPath(String path) {
        this.path = path;
    }

    public HttpQuery getQuery() {
        int questionPos = path.indexOf("?");
        String query = path.substring(questionPos+1);
        return new HttpQuery(query);
    }
}
