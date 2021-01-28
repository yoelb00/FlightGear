package view;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import viewmodel.ViewModel;

public class MainWindowController implements Initializable, Observer{
    public ViewModel vm;
    Stage stage = new Stage();
    public DoubleProperty x;
    public DoubleProperty y;
    public DoubleProperty xmap, ymap;
    public DoubleProperty offset;
    public DoubleProperty heading;
    public DoubleProperty aileron;
    public DoubleProperty elevator;
    public DoubleProperty planeX;
    public DoubleProperty planeY;
    public int mazeData[][];
    private Image plane[];
    private Image mark;
    public double lastX;
    public double lastY;
    private String[] solution;
    private BooleanProperty path;
	public BooleanProperty isConnectedToSimulator;
	private boolean mapStatus=false;
	public StringProperty ipPath;
	public StringProperty portPath;
	public boolean isConnenctCalcPath=false;
    @FXML
    public TextField varIP,varPort ;
    @FXML
    Slider throttle;
    @FXML
    Slider rudder;
    @FXML
    Circle outCircle;
    @FXML
    Circle insideCircle;
    @FXML
    RadioButton manual;
    @FXML
    RadioButton autopilot;
    @FXML
    TextArea autopilotarea;
    @FXML
    public MazeDisplay mapArea;
    @FXML
    public Canvas airplaneCanvas;
    @FXML
    private Canvas markCanvas;
    @FXML
    private Button inConnectBTN;
	private static int calcPathOrConnect;
    //The Joystick variables:
    double orgSceneX;
    double orgSceneY;
    double orgTranslateX;
    double orgTranslateY;
    public MainWindowController(){
    	aileron = new SimpleDoubleProperty();
    	elevator = new SimpleDoubleProperty();
    	mapArea = new MazeDisplay();
    	autopilotarea = new TextArea();
        x = new SimpleDoubleProperty();
        y = new SimpleDoubleProperty();
        offset = new SimpleDoubleProperty();
    	planeX = new SimpleDoubleProperty();
        planeY = new SimpleDoubleProperty();
        heading = new SimpleDoubleProperty();
        plane = new Image[8];
		xmap = new SimpleDoubleProperty();
		ymap = new SimpleDoubleProperty();
		path = new SimpleBooleanProperty();
		isConnectedToSimulator = new SimpleBooleanProperty();
    }
	public void setViewModel(ViewModel vm){
		this.vm = vm;
		this.vm.throttle.bind(throttle.valueProperty());
		this.vm.rudder.bind(rudder.valueProperty());
		this.vm.aileron.bind(aileron);
		this.vm.elevator.bind(elevator);
        heading.bindBidirectional(vm.heading);
        x.bindBidirectional(vm.x);
        y.bindBidirectional(vm.y);
        offset.bindBidirectional(vm.offset);
        heading.bindBidirectional(vm.heading);
        ymap.bindBidirectional(vm.ymap);
        xmap.bindBidirectional(vm.xmap);
        path.bindBidirectional(vm.path);
        path.setValue(false);
        isConnectedToSimulator.set(false);
		planeX.bindBidirectional(vm.planeX);
        planeY.bindBidirectional(vm.planeY);
        try {
            plane[0]=new Image(new FileInputStream("./resources/plane0.png"));
            plane[1]=new Image(new FileInputStream("./resources/plane45.png"));
            plane[2]=new Image(new FileInputStream("./resources/plane90.png"));
            plane[3]=new Image(new FileInputStream("./resources/plane135.png"));
            plane[4]=new Image(new FileInputStream("./resources/plane180.png"));
            plane[5]=new Image(new FileInputStream("./resources/plane225.png"));
            plane[6]=new Image(new FileInputStream("./resources/plane270.png"));
            plane[7]=new Image(new FileInputStream("./resources/plane315.png"));
            mark = new Image(new FileInputStream("./resources/mark.png"));
        } catch (FileNotFoundException e) { e.printStackTrace(); }
	}
	//This will open the connect popup
    	public void OnConnectPress() throws Exception {
    	Parent root1 = null;
    	//	Stage stage = new Stage();
    		try {
    	        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConnectPopUpView.fxml"));
    	        root1 =  (Parent) fxmlLoader.load();
    	        MainWindowController mwc = fxmlLoader.getController();
    	        mwc.vm = this.vm;
    	        stage.setScene(new Scene(root1));
    	        //if the window already open
    	        if(!stage.isShowing()){
    	        	stage.show();
    	        	calcPathOrConnect = 2;
    	        }
    	    } catch(Exception e) {}
    	}
		@Override
		public void update(Observable o, Object arg) {
			// TODO Auto-generated method stub
			if (o == vm)
			{	//airPlane:
				if(arg==null)
	                DrawAirPlaneInTheMap();
	            else //linePath:
	            {
	                solution=(String[])arg;
	                this.DrawAirPlaneGPSLine();
	            }
			}
	     }
		//When i press the joystick
		public void insideCircleClick(MouseEvent event){
			if(manual.isSelected()){
		orgSceneX = event.getSceneX();
		orgSceneY = event.getSceneY();
		orgTranslateX = ((Circle)(event.getSource())).getTranslateX();
		orgTranslateY = ((Circle)(event.getSource())).getTranslateY();
			}

		}
		//When i realse the joystick
		public void insideCircleRelease(MouseEvent event){
			if(manual.isSelected()){
				((Circle)(event.getSource())).setTranslateX(orgTranslateX);
	            ((Circle)(event.getSource())).setTranslateY(orgTranslateY);
                aileron.setValue(0);
                elevator.setValue(0);
                vm.setAileronAndElevator();
			}

		}
		public void manual() {
			if(autopilot.isSelected()){
				autopilot.setSelected(false);
			}
			manual.setSelected(true);
			this.vm.StopAutoPilot();
		}

		public void autoPilot(){
			if(manual.isSelected())
			{
				manual.setSelected(false);
			}
			autopilot.setSelected(true);
		}
		public void LoadFIle(){
			if(autopilot.isSelected()){
				autopilotarea.clear();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("./"));
				int view = fileChooser.showOpenDialog(null);
				ArrayList<String> list = new ArrayList<>();

				if(view == JFileChooser.APPROVE_OPTION){
					try{
						Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileChooser.getSelectedFile())));

						while(scanner.hasNextLine()== true){
							String line = scanner.nextLine();
							list.add(line + "\n");
							autopilotarea.appendText(line + "\n");
						}
						scanner.close();
							this.vm.startAutoPilot(list);
					}
					catch (FileNotFoundException e) {}
				}

			}
		}
		//when i move the joystick
		public void insideCircleMove(MouseEvent event){
			if(manual.isSelected()){
			double offsetX = event.getSceneX() - orgSceneX;
			double offsetY = event.getSceneY() - orgSceneY;
			double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
            if(((Math.pow((newTranslateX - outCircle.getCenterX()), 2) + Math.pow(( newTranslateY - outCircle.getCenterY()), 2))) <= (Math.pow(outCircle.getRadius() - insideCircle.getRadius(), 2))){
            	((Circle) (event.getSource())).setTranslateX(newTranslateX);
                ((Circle) (event.getSource())).setTranslateY(newTranslateY);
                double maxx = (outCircle.getRadius() - insideCircle.getRadius()) + outCircle.getCenterX();
                double minx = outCircle.getCenterX() - (outCircle.getRadius() - insideCircle.getRadius());
                double new_maxx = 1;
                double new_minx = -1;
                double rex =  (((newTranslateX - minx) / (maxx - minx) * (new_maxx - new_minx) + new_minx));
                aileron.setValue(rex);
                double miny = (outCircle.getRadius() - insideCircle.getRadius()) + outCircle.getCenterY();
                double maxy = outCircle.getCenterY() - (outCircle.getRadius() - insideCircle.getRadius());
                double new_maxy = 1;
                double new_miny =-1;
                double rey =  (((newTranslateY - miny) / (maxy - miny) * (new_maxy - new_miny) + new_miny));
                elevator.setValue(rey);
                vm.setAileronAndElevator();
            	}
			}
		}
    	  public void calculate() throws Exception {
    		Parent root1 = null;
    		try {
    	        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConnectPopUpView.fxml"));
    	        root1 =  (Parent) fxmlLoader.load();
    	        MainWindowController mwc = fxmlLoader.getController();
    	        mwc.vm = this.vm;
    	        mwc.mapArea = this.mapArea;
    	        mwc.mazeData = this.mazeData;
    	        mwc.markCanvas = this.markCanvas;
    	        mwc.airplaneCanvas = this.airplaneCanvas;
    	        mwc.planeX =this.planeX;
    	        mwc.planeY= this.planeY;
    	        mwc.heading = this.heading;
    	        mwc.lastX = this.lastX;
    	        mwc.lastY = this.lastY;
    	        mwc.plane = this.plane;
                mwc.path = new SimpleBooleanProperty();
                mwc.path.bindBidirectional(this.path);
    	        stage.setScene(new Scene(root1));
    	        if(!stage.isShowing()){
    	        	calcPathOrConnect = 1;
    	        	stage.show();
    	        }
    	    } catch(Exception e) {}
    	}

		//this is the connect in the popup
        public void OnConnectInThePopUp(){
    		this.vm.ip.bind(varIP.textProperty());
    		this.vm.port.bind(varPort.textProperty());
    		//Connect button call me
    		if(calcPathOrConnect == 2){
    			vm.connect();
    			Stage stage = (Stage) inConnectBTN.getScene().getWindow();
                stage.close();
    		}
    		//calcPath button call me
    		if(calcPathOrConnect == 1){
				double H = markCanvas.getHeight();
				double W = markCanvas.getWidth();
				double h = H / mazeData.length;
				double w = W / mazeData[0].length;
				if(this.vm.ip.getValue()!=null && this.vm.port.getValue()!=null)
				{
					vm.findPlaneGPSPath(h, w);
					path.setValue(true);
				}
	            Stage stage = (Stage)inConnectBTN.getScene().getWindow();
	            stage.close();
    		}
        }
	    @FXML
		public void LoadDataCSV()
		{
			FileChooser fc = new FileChooser();
			fc.setTitle("Load MAP");
			fc.setInitialDirectory(new File("./resources"));
			FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
			fc.getExtensionFilters().add(fileExtensions);
			File selectedFile = fc.showOpenDialog(null);
	        if (selectedFile != null)
	        {
	            BufferedReader br = null;
	            String line = "";
	            String cvsSplitBy = ",";

	            List<String[]> numbers = new ArrayList<>();
	            try
	            {
	                br = new BufferedReader(new FileReader(selectedFile));
	                String[] start=br.readLine().split(cvsSplitBy);
	                x.setValue(Double.parseDouble(start[0]));
	                y.setValue(Double.parseDouble(start[1]));
	                start=br.readLine().split(cvsSplitBy);
	                offset.setValue(Double.parseDouble(start[0]));
	                while ((line = br.readLine()) != null)
	                    numbers.add(line.split(cvsSplitBy));
	                mazeData = new int[numbers.size()][];
	                for (int i = 0; i < numbers.size(); i++)
	                {
	                	mazeData[i] = new int[numbers.get(i).length];
	                    for (int j = 0; j < numbers.get(i).length; j++)
	                    {
	                        String tmp=numbers.get(i)[j];
	                        mazeData[i][j] = Integer.parseInt(tmp);
	                    }
	                }
	                this.vm.setMatrixData(mazeData);
	                this.DrawAirPlaneInTheMap();
	                mapArea.setMazeData(mazeData);
	                mapStatus=true;
	            }
	            catch (FileNotFoundException e) {}
	            catch (IOException e) {}
	        }
		}
	    public void DrawAirPlaneInTheMap()
	    {
	        if(planeX.getValue()!=null&&planeY.getValue()!=null)
	        {
	            double H = airplaneCanvas.getHeight();
	            double W = airplaneCanvas.getWidth();
	            double h = H / mazeData.length;
	            double w = W / mazeData[0].length;
	            GraphicsContext gc = airplaneCanvas.getGraphicsContext2D();
	            lastX=planeX.getValue();
	            lastY=planeY.getValue()*-1;
	            gc.clearRect(0,0,W,H);

	            if(heading.getValue()>=0&&heading.getValue()<39)
	                gc.drawImage(plane[0], w*lastX, lastY*h, 25, 25);
	            if(heading.getValue()>=39&&heading.getValue()<80)
	                gc.drawImage(plane[1], w*lastX, lastY*h, 25, 25);
	            if(heading.getValue()>=80&&heading.getValue()<129)
	                gc.drawImage(plane[2], w*lastX, lastY*h, 25, 25);
	            if(heading.getValue()>=129&&heading.getValue()<170)
	                gc.drawImage(plane[3], w*lastX, lastY*h, 25, 25);
	            if(heading.getValue()>=170&&heading.getValue()<219)
	                gc.drawImage(plane[4], w*lastX, lastY*h, 25, 25);
	            if(heading.getValue()>=219&&heading.getValue()<260)
	                gc.drawImage(plane[5], w*lastX, lastY*h, 25, 25);
	            if(heading.getValue()>=260&&heading.getValue()<309)
	                gc.drawImage(plane[6], w*lastX, lastY*h, 25, 25);
	            if(heading.getValue()>=309)
	                gc.drawImage(plane[7], w*lastX, lastY*h, 25, 25);
	        }
	    }
	    public void drawMark()
	    {
	        double H = markCanvas.getHeight();
	        double W = markCanvas.getWidth();
	        double h = H / mazeData.length;
	        double w = W / mazeData[0].length;
	        GraphicsContext gc = markCanvas.getGraphicsContext2D();
	        gc.clearRect(0,0,W,H);
	        gc.drawImage(mark, xmap.getValue()-13,ymap.getValue() , 10, 10);
	        if(path.getValue())
	            vm.findPlaneGPSPath(h,w);
	    }
	    public void DrawAirPlaneGPSLine()
	    {
	        double H = markCanvas.getHeight();
	        double W = markCanvas.getWidth();
	        double h = H / mazeData.length;
	        double w = W / mazeData[0].length;
	        GraphicsContext gc=markCanvas.getGraphicsContext2D();
	        String move=solution[1];
	        double x= planeX.getValue()*w+10*w;
	        double y=planeY.getValue()*-h+6*h;
	        for(int i=2;i<solution.length;i++)
	        {
	            switch (move)
	            {
	                case "Right":
	                    gc.setStroke(Color.BLACK.darker());
	                    gc.strokeLine(x, y, x + w, y);
	                    x +=  w;
	                    break;
	                case "Left":
	                    gc.setStroke(Color.BLACK.darker());
	                    gc.strokeLine(x, y, x -  w, y);
	                    x -=  w;
	                    break;
	                case "Up":
	                    gc.setStroke(Color.BLACK.darker());
	                    gc.strokeLine(x, y, x, y - h);
	                    y -=  h;
	                    break;
	                case "Down":
	                    gc.setStroke(Color.BLACK.darker());
	                    gc.strokeLine(x, y, x, y +  h);
	                    y += h;
	            }
	            move=solution[i];
	        }
	    }
	    EventHandler<MouseEvent> mapClick = new EventHandler<MouseEvent>()
	    {
	        @Override
	        public void handle(MouseEvent e)
	        {
	        	if(mapStatus) {
		            xmap.setValue(e.getX());
		            ymap.setValue(e.getY());
		            drawMark();
	        	}
	        }
	    };
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub
			//The if check if i'm the mainwindowfxml and not the popup
			//if i dont put the if here so the pop up will run this func
			if(location.getPath().contains("MainWindow.fxml")){
				//event for the throttle
				throttle.valueProperty().addListener((ChangeListener<Object>) (a1,a2,a3) -> {
					if(this.manual.isSelected()){
						vm.setThrottle();
					}
					});
				//event for the rudder
				rudder.valueProperty().addListener((ChangeListener<Object>) (a1,a2,a3) -> {
					if(this.manual.isSelected()){
					vm.setRudder();
					}
					});
				markCanvas.setOnMouseClicked(mapClick);
			}
		}
}
