module com.example.downloaderinfo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires rome;
    requires rome.fetcher;


    opens com.example.downloaderinfo to javafx.fxml;
    exports com.example.downloaderinfo;
}