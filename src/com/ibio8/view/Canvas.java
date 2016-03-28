package com.ibio8.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import com.ibio8.controller.Controller;
import com.ibio8.model.vo.TrackVO;

public class Canvas extends Application {
	private ListView<String> _playlist;
	private Button _btnPlayPause;
	private int _lastIndex = -1;
	private Text _up2Line;
	private Text _upLine;
	private Text _currentLine;
	private Text _nextLine;
	private Text _next2Line;
	
	public Canvas(){
		Controller.getInstance().initView(this);
	}
	
	//the app starts from here
	public static void main(String[] args) {
        launch(args);
    }
	
    @Override
    public void start(Stage primaryStage) {
        BorderPane border = new BorderPane();
        HBox hbox = createControlBox();
        addStackPane(hbox);
        border.setTop(hbox);
        border.setLeft(createMenuBox());
        border.setCenter(createLyrics());
        border.setRight(createPlaylist());
        
        StackPane root = new StackPane();
        root.getChildren().add(border);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Lyrics MP3 Player");
        primaryStage.setScene(scene);
        primaryStage.show();
        //ATTENTION: needs to finish GUI for upcoming activities.
		//System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Controller.getInstance().search(System.getProperty("user.dir"));
    }
	
	public void showContent(String html){
		//_showPane.setText(html);
	}
	
	public void showLyrics(List<String> list, int index){
		if(list != null && index >= 0){
			if(index - 2 >= 0){
				_up2Line.setText(list.get(index - 2) + "\n");
			}
			if(index - 1 >= 0){
				_upLine.setText(list.get(index - 1) + "\n");
			}
			_currentLine.setText(list.get(index) + "\n");
			if(index + 1 < list.size()){
				_nextLine.setText(list.get(index + 1) + "\n");
			}
			if(index + 2 < list.size()){
				_next2Line.setText(list.get(index + 2) + "\n");
			}
		}else{
			_up2Line.setText("");
			_upLine.setText("");
			_currentLine.setText("");
			_nextLine.setText("");
			_next2Line.setText("");
		}
	}
	
	public void buildTracks(List<TrackVO> list){
		ArrayList<String> data = new ArrayList<String>();
		for(TrackVO item : list){
			data.add(item.title);
		}
		ObservableList<String> items = FXCollections.observableArrayList(data);
    	_playlist.setItems(items);
	}
	
	public void changeCurrentItem(int index, TrackVO track){
		//_playlist.changeIndex(index);
		_playlist.scrollTo(index);
		_playlist.getSelectionModel().select(index);
		_lastIndex = index;
		//since it's auto-play
		_btnPlayPause.setText("Pause");
	}
	
	private HBox createControlBox(){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");
        
        Button btnPrevious = new Button();
        //btnPrevious.setPrefSize(70, 0);
        btnPrevious.setStyle("-fx-background-radius: 5em; -fx-min-width: 70px; -fx-min-height: 35px; -fx-max-width: 70px; -fx-max-height: 35px;");
        btnPrevious.setText("◀ Prev");
        btnPrevious.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Controller.getInstance().previous();
            }
        });
        
        //
        _btnPlayPause = new Button();
        //_btnPlayPause.setPrefSize(80, 20);
        _btnPlayPause.setText("Ready");
        _btnPlayPause.setStyle("-fx-background-radius: 5em; -fx-min-width: 80px; -fx-min-height: 35px; -fx-max-width: 80px; -fx-max-height: 35px;");
        _btnPlayPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Controller.getInstance().playPause();
                if(_btnPlayPause.getText().equals("Pause")){
                	_btnPlayPause.setText("Play");
                }else{
                	_btnPlayPause.setText("Pause");
                }
            }
        });
        
        Button btnNext = new Button();
        //btnNext.setPrefSize(70, 30);
        btnNext.setStyle("-fx-background-radius: 5em; -fx-min-width: 70px; -fx-min-height: 35px; -fx-max-width: 70px; -fx-max-height: 35px;");
        btnNext.setText("Next ▶");
        btnNext.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Controller.getInstance().next();
            }
        });
     
        hbox.getChildren().addAll(btnPrevious, _btnPlayPause, btnNext);
        return hbox;
    }
    
    private VBox createMenuBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Text title = new Text("Menu");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        Hyperlink options[] = new Hyperlink[] {
            new Hyperlink("Lyrics"),
            new Hyperlink("Info"),
            new Hyperlink("Distribution"),
            new Hyperlink("Costs")};

        for (int i=0; i<4; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }
        return vbox;
    }
    
    private void addStackPane(HBox hb){
        StackPane stack = new StackPane();
        Rectangle bg = new Rectangle(30.0, 25.0);
        bg.setFill(new LinearGradient(0,0,0,1, true, CycleMethod.NO_CYCLE,
            new Stop[]{
            new Stop(0,Color.web("#4977A3")),
            new Stop(0.5, Color.web("#B0C6DA")),
            new Stop(1,Color.web("#9CB6CF")),}));
        bg.setStroke(Color.web("#D0E6FA"));
        bg.setArcHeight(3.5);
        bg.setArcWidth(3.5);
        
        Text text = new Text("I");
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        text.setFill(Color.WHITE);
        text.setStroke(Color.web("#7080A0")); 

        Button btn = new Button();
        btn.setPrefSize(30, 25);
        btn.setStyle("-fx-background-color: rgba(0,0,0,0);");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        stack.getChildren().addAll(bg, text, btn);
        stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack
        StackPane.setMargin(text, new Insets(0, 10, 0, 0)); // Center "?"

        hb.getChildren().add(stack);            // Add stack pane to HBox object
        HBox.setHgrow(stack, Priority.ALWAYS);    // Give stack any extra space
    }
    
    private VBox createLyrics(){
    	VBox box = new VBox();
    	box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(0, 10, 0, 10));
        
        TextFlow flow = new TextFlow();
        flow.setTextAlignment(TextAlignment.CENTER);
        flow.setPrefWidth(350);
        flow.setLineSpacing(10);
        
        _up2Line = new Text();
        _up2Line.setStyle("-fx-font-weight: normal");
        _upLine = new Text();
        _upLine.setStyle("-fx-font-weight: normal");
        _currentLine = new Text();
        _currentLine.setStyle("-fx-font-weight: bold");
        _nextLine = new Text();
        _nextLine.setStyle("-fx-font-weight: normal");
        _next2Line = new Text();
        _next2Line.setStyle("-fx-font-weight: normal");

        flow.getChildren().addAll(_up2Line, _upLine, _currentLine, _nextLine, _next2Line);
        box.getChildren().add(flow); 
        return box;
    }
    
    private VBox createPlaylist(){
    	VBox box = new VBox();
    	box.setPrefWidth(200);
    	_playlist = new ListView<>();
    	_playlist.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	int index = _playlist.getSelectionModel().getSelectedIndex(); 
            	if(index != _lastIndex){
            		Controller.getInstance().change(index);
            		_lastIndex = index;
            	}
            }
        });
    	VBox.setVgrow(_playlist, Priority.ALWAYS);
    	box.getChildren().add(_playlist);
    	return box;
    }
}
