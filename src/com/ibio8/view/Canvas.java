package com.ibio8.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
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
import javafx.util.Duration;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import com.ibio8.controller.Controller;
import com.ibio8.model.vo.TrackVO;

public class Canvas extends Application {
	final private ListView<String> _playlist = new ListView<>();
	final private Button _btnPlayPause = new Button();
	final private Text _info = new Text();
	final private Text _up2Line = new Text();
	final private Text _upLine = new Text();
	final private Text _currentLine = new Text();
	final private Text _nextLine = new Text();
	final private Text _next2Line = new Text();
	final private Slider _progressBar = new Slider(0, 1, 0);
	private int _lastIndex = -1;
	private float _currentProgress;
	private boolean _lockSlider = false;
	private String _infoText;
	private boolean _showLyrics = true;
	
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
        border.setCenter(createContent());
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
    
	/**
	 *  Close down the application
	 *  @see
	 *   -- http://docs.oracle.com/javafx/2/api/javafx/application/Application.html#stop%28%29
	 **/
	@Override
	public void stop(){
		Controller.getInstance().shutdown();
		Platform.exit();
		
	     //for( ScheduledExecutorService sched : activeExecutorServices ){
	         //sched.shutdown();
	     //}
	 }
	
	public void showContent(String text){
		_infoText = text;
		if(!_showLyrics){
			_info.setText(_infoText);
		}
	}
	
	public void showLyrics(List<String> list, int index){
		if(_showLyrics && list != null && index >= 0){
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
	
	public void updateProgress(Duration playingTime, Duration totalTime){
		_currentProgress = (float) (playingTime.toSeconds() / totalTime.toSeconds());
		//TODO this part still has a problem to distinguish if it's manually changed value.
		if(!_lockSlider){
			_progressBar.setValue(_currentProgress);
		}
	}
	
	private HBox createControlBox(){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setAlignment(Pos.CENTER);
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
        
        //_btnPlayPause.setPrefSize(80, 20);
        _btnPlayPause.setText("Ready");
        _btnPlayPause.setStyle("-fx-background-radius: 5em; -fx-min-width: 100px; -fx-min-height: 55px; -fx-max-width: 80px; -fx-max-height: 35px;");
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
        
        //progress bar
        _progressBar.setPrefWidth(200);
        _progressBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
            	float value = observableValue.getValue().floatValue();
            	_lockSlider = (_currentProgress != value);
            	//System.out.println("_lockSlider " + _lockSlider);
            	if(_lockSlider){
            		Controller.getInstance().seek(value);
                	_lockSlider = false;
            	}
            }
        });
        hbox.getChildren().addAll(btnPrevious, _btnPlayPause, btnNext, _progressBar);
        return hbox;
    }
    
    private VBox createMenuBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Text title = new Text("Menu");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);
        
        //
        final ToggleGroup group = new ToggleGroup();
        RadioButton rb1 = new RadioButton("Lyrics");
        rb1.setToggleGroup(group);
        rb1.setUserData("lyrics");
        //default shows lyrics
        rb1.setSelected(true);
        RadioButton rb2 = new RadioButton("Info");
        rb2.setToggleGroup(group);
        rb2.setUserData("info");
        RadioButton options[] = new RadioButton[] {rb1, rb2};
        //radio group click handler
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
              if (group.getSelectedToggle() != null) {
                //System.out.println(group.getSelectedToggle().getUserData().toString());
                switch(group.getSelectedToggle().getUserData().toString()){
                	case "lyrics":
                		_info.setText("");
                		_showLyrics = true;
                		break;
                	case "info":
                		_info.setText(_infoText);
                		_showLyrics = false;
                		break;
                }
              }
            }
          });
        for (int i = 0; i < options.length; i++) {
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
                //System.out.println("Open a URL");
            	URI uri = null;
				try {
					uri = new URI("http://ibio.github.io");
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
            	Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(uri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
            }
        });
        
        stack.getChildren().addAll(bg, text, btn);
        stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack
        StackPane.setMargin(text, new Insets(0, 10, 0, 0)); // Center "?"

        hb.getChildren().add(stack);            // Add stack pane to HBox object
        HBox.setHgrow(stack, Priority.ALWAYS);    // Give stack any extra space
    }
    
    private VBox createContent(){
    	VBox box = new VBox();
    	box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(0, 10, 0, 10));
        
        TextFlow flow = new TextFlow();
        flow.setTextAlignment(TextAlignment.CENTER);
        flow.setPrefWidth(350);
        flow.setLineSpacing(10);
        
        _up2Line.setStyle("-fx-font-weight: normal");
        _upLine.setStyle("-fx-font-weight: normal");
        _currentLine.setStyle("-fx-font-weight: bold");
        _nextLine.setStyle("-fx-font-weight: normal");
        _next2Line.setStyle("-fx-font-weight: normal");

        flow.getChildren().addAll(_info, _up2Line, _upLine, _currentLine, _nextLine, _next2Line);
        //flow.getChildren().add(new Text("\"La La La\" is a single by British producer Naughty Boy, featuring vocals from Sam Smith. It was released on 18 May 2013 as the second single from Naughty Boy's debut album Hotel Cabana (2013) and the deluxe version of Smith's debut album In the Lonely Hour. The track reached number one in 26 countries, including on the UK Singles Chart."));
        box.getChildren().add(flow); 
        return box;
    }
    
    private VBox createPlaylist(){
    	VBox box = new VBox();
    	box.setPrefWidth(200);
    	//
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
