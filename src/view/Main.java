package view;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.SimModel;
import model.Command.OpenServerCommand;
/*import model.Command.OpenServerCommand;
import model.Interpeter.Utilities;
import model.Server.DataReaderServer;
import model.Server.CacheManager;
import model.Server.ClientHandlerPath;
import model.Server.FileCacheManager;
import model.Server.MyClientHandler;
import model.Server.MySerialServer;
import model.Server.Server;*/
import viewmodel.ViewModel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage stage) throws IOException {

		OpenServerCommand server=new OpenServerCommand();
		try {
			server.execute(new String[] {"5400","10"});
		} catch (Exception e) {e.printStackTrace();}
		FXMLLoader load = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = load.load();
        MainWindowController mwc = load.getController();
        SimModel simM = new SimModel();
        ViewModel viewModel = new ViewModel(simM);
        simM.addObserver(viewModel);
        viewModel.addObserver(mwc);
        mwc.setViewModel(viewModel);
        /*Server serverpath = new MySerialServer();
        CacheManager CacheManager = new FileCacheManager();
        MyClientHandler ch = new MyClientHandler(CacheManager);
        serverpath.open(5454, new ClientHandlerPath(ch));*/
        stage.setTitle("Simulator Controller");
        stage.setScene(new Scene(root));
        stage.show();
	}

	public static void main(String[] args) {
		launch(args);
		Platform.exit();
		System.exit(0);
	}
}
