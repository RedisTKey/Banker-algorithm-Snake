import javax.swing.*;

public class Food {
	Vertex position;
	foodImageState imageState;
	//指明生成食物的状态？是R，是B还是G

	ImageIcon food_R = new ImageIcon("src/Images/Food_R.png");
	//导入food_R，导入了R类型的食物图片
	ImageIcon food_G = new ImageIcon("src/Images/Food_G.png");
	//导入food_G，导入了G类型的食物图片
	ImageIcon food_B = new ImageIcon("src/Images/Food_B.png");
	//导入food_B，导入了B类型的食物图片


	enum foodImageState{
		RED,BLUE,GREEN
	}
	public Food() {
		//默认情况下，生成的食物是R类型
		imageState = foodImageState.RED;
		position = new Vertex(32 * (int)(Math.random() * 34 + 1),96 + 32 * (int)(Math.random() * 23));

	}

	public Food(foodImageState state) {

		this.imageState = state;
		position = new Vertex(32 * (int)(Math.random() * 34 + 1),96 + 32 * (int)(Math.random() * 23));

	}

	void beAte() {

		position = new Vertex(32 * (int)(Math.random() * 34 + 1),96 + 32 * (int)(Math.random() * 23));


		int randomType = (int)(Math.random() * 3); // 0, 1, 2
		switch (randomType) {
			case 0:

				imageState = foodImageState.RED;
				break;

			case 1:
				imageState = foodImageState.BLUE;
				break;


			case 2:
				imageState = foodImageState.GREEN;
				break;
		}


	}

	public ImageIcon getImage(){
		switch (imageState){
			case GREEN:
				return food_G;
			case BLUE:
				return food_B;
			case RED:
				return food_R;
			default:
				// Handle the default case, for example, return a default image or throw an exception
				return null;
		}
	}

	public foodImageState getImageState() {
		return imageState;
	}

	public void randomLocation(){
		position = new Vertex(32 * (int)(Math.random() * 34 + 1),96 + 32 * (int)(Math.random() * 23));
	}
}
