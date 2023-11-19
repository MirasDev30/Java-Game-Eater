module com.example.eaternew {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.eaternew to javafx.fxml;
    exports com.example.eaternew;
}