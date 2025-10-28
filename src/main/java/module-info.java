module org.example {
    requires javafx.controls;
    requires com.google.gson;
    exports org.example;

    opens org.example.Model to com.google.gson;
}