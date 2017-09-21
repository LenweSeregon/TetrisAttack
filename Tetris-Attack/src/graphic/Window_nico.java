package graphic;

import javax.swing.JFrame;

import mvc_controller.New_Controller;
import mvc_global_enums.Grid_position;
import mvc_model.Grid;
import mvc_view.Board_view;

public class Window_nico extends JFrame {

	private Thread thread;

	public Window_nico() {

		Grid model_left = new Grid(13, 6, 40, Grid_position.LEFT);
		New_Controller ctrl_left = new New_Controller(model_left, "LEFT");

		Grid model_right = new Grid(13, 6, 40, Grid_position.RIGHT);
		New_Controller ctrl_right = new New_Controller(model_right, "RIGHT");

		Board_view view = new Board_view(12, 6, 40, ctrl_left, ctrl_right);

		model_left.add_observer(view);
		model_right.add_observer(view);

		ctrl_left.init_grid();
		ctrl_right.init_grid();

		ctrl_left.bind_controller(ctrl_right);
		ctrl_right.bind_controller(ctrl_left);

		new Thread(ctrl_left).start();
		new Thread(ctrl_right).start();

		this.setTitle("Tetris-Attack");
		this.setSize(1000, 700);
		this.setFocusable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setContentPane(view);
		this.pack();
		this.setVisible(true);

		view.setFocusable(true);
		view.requestFocusInWindow();
	}

	private void close_window() {
		this.dispose();
	}
}
