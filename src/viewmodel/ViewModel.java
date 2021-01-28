package viewmodel;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import model.SimModel;
import javafx.beans.property.*;
public class ViewModel extends Observable implements Observer{
	SimModel m;
	public DoubleProperty xmap, ymap;
	public BooleanProperty path;
	public StringProperty ip, port;
	public DoubleProperty throttle;
	public DoubleProperty rudder;
    public DoubleProperty aileron;
    public DoubleProperty elevator;
    private int data[][];
    public DoubleProperty heading;
    public DoubleProperty x;
    public DoubleProperty y;
    public DoubleProperty offset;
    public DoubleProperty planeX;
    public DoubleProperty planeY;
	public ViewModel(SimModel m){
		this.m = m;
        ip = new SimpleStringProperty();
        port = new SimpleStringProperty();
        throttle = new SimpleDoubleProperty();
        rudder = new SimpleDoubleProperty();
		aileron = new SimpleDoubleProperty();
		elevator = new SimpleDoubleProperty();
        path=new SimpleBooleanProperty();
		heading = new SimpleDoubleProperty();
        x = new SimpleDoubleProperty();
        y = new SimpleDoubleProperty();
        offset = new SimpleDoubleProperty();
        planeX = new SimpleDoubleProperty();
        planeY = new SimpleDoubleProperty();
        xmap=new SimpleDoubleProperty();
        ymap=new SimpleDoubleProperty();
	}
    public void connect() {
        m.connect(ip.get(), Integer.parseInt(port.get()));
    }
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
        if(arg0==m)
		{
			 String[] tmp=(String[])arg1;
	            if(tmp[0].equals("plane"))
	            {
	                double xx = Double.parseDouble(tmp[1]);
	                double yy = Double.parseDouble(tmp[2]);
	                xx = (xx - x.getValue() + offset.getValue());
	                xx /= offset.getValue();
	                yy = (yy - y.getValue() + offset.getValue()) / offset.getValue();
	                planeX.setValue(xx);
	                planeY.setValue(yy);
	                heading.setValue(Double.parseDouble(tmp[3]));
	                setChanged();
	                notifyObservers();
	            }
	            else if(tmp[0].equals("path"))
	            {
	                setChanged();
	                notifyObservers(tmp);
	            };
		}
	}
	public void startAutoPilot(List<String> lines){
		this.m.autoPilot(lines);
	}
	public void StopAutoPilot(){
		this.m.StopAutoPilot();
	}
    public void setThrottle() {
        String[] temp = { "set /controls/engines/current-engine/throttle "+throttle.getValue() };
        m.Send(temp);
    }
	public void setRudder() {
        String[] temp = { "set /controls/flight/rudder "+rudder.getValue()};
        m.Send(temp);
	}
	public void setAileronAndElevator() {
        String[] temp = { "set /controls/flight/aileron "+aileron.getValue(),
         "set /controls/flight/elevator "+elevator.getValue(),};
        m.Send(temp);
	}
	public void setMatrixData(int[][] data)
	{
		this.data = data;
		m.GetAirPlaneLocation(x.getValue(), y.doubleValue(), offset.getValue());
	}
	public void findPlaneGPSPath(double h, double w)
	{
		if (!path.getValue())
			m.connectPath(ip.getValue(), Integer.parseInt(port.getValue()));
		m.findPlaneGPSPath((int) (planeY.getValue() / -1), (int) (planeX.getValue() + 15),
				Math.abs((int) (ymap.getValue() / h)), Math.abs((int) (xmap.getValue() / w)), data);
	}
}