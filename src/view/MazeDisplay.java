package view;

import javafx.scene.paint.Color;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class MazeDisplay extends Canvas {

	int[][] mazeData;
	double max, min;


	public void setMazeData(int[][] mazeData) {

		this.mazeData = mazeData;
		this.min = Double.MAX_VALUE;
		this.max = 0;

		for(int i= 0; i< mazeData.length; i++)
		{
			for(int j=0; j<mazeData[i].length; j++)
			{
                if(min>mazeData[i][j])
                {
                	min=mazeData[i][j];
                }
                if(max<mazeData[i][j])
                {
                    max=mazeData[i][j];
                }
			}
		}

        double new_max = 255;
        double new_min = 0;

        for (int i = 0; i < mazeData.length; i++) {
            for (int j = 0; j < mazeData[i].length; j++) {
            	mazeData[i][j] = (int)((mazeData[i][j] - min) / (max - min) * (new_max - new_min) + new_min);
            }
        }
		//We need to change the values to void negative numbers
		redraw();
	}




	public void redraw(){
		if(mazeData != null){
			double W = getWidth();
			double H = getHeight();
			double w = W / mazeData[0].length;
			double h = H / mazeData.length;
			GraphicsContext gc = getGraphicsContext2D();
			//clear the map before draw new
				//gc.clearRect(0, 0, W, H);

			for(int i = 0; i < mazeData.length; i++){
				for(int j = 0; j < mazeData[i].length; j++)
				{
					if(mazeData[i][j] != 0)
					{
						int t = mazeData[i][j];
						gc.setFill(Color.rgb((255- t),(0 + t),0));
						gc.fillRect((j * w), (i * h), w, h);
					}
					else{
						gc.setFill(Color.rgb(255,0,0));
	                    gc.fillRect(j*w,i*h,w,h);
					}
				}
			}
		}
	}



}
