public class Snake {
	protected int length = 3;
	protected int boundX_1 = 1088,boundX_2 = 32, boundY_1 = 800,boundY_2 = 96;

	protected Vertex[] body;
	protected Direciton direction;
	private boolean isCompleted = false;

	private int MAX_R,MAX_G,MAX_B;

	enum Direciton {
		RIGHT,LEFT,UP,DOWN
	}

	public Snake (int MAX_R,int R, int MAX_G,int G, int MAX_B,int B){
		this.MAX_R = MAX_R;
		this.MAX_G = MAX_G;
		this.MAX_B = MAX_B;
		direction = Direciton.RIGHT;
		this.length = MAX_R+MAX_G+MAX_B+1;
		this.body = new Vertex[length];
		body[0] = new Vertex(0,0);
		body[0].setImageState(Vertex.ImageState.HEAD_R);
		body[0].filling();
		for (int i = 1; i < MAX_R+1; i++) {
			body[i] = new Vertex(0,0);
			if (i<R+1) {
				body[i].setImageState(Vertex.ImageState.BODY_R_F);
				body[i].filling();
			}
			else
				body[i].setImageState(Vertex.ImageState.BODY_R_E);
		}
		for (int i = MAX_R+1; i < MAX_R+MAX_G+1; i++) {
			body[i] = new Vertex(0,0);
			if (i<MAX_R+G+1)
			{
				body[i].setImageState(Vertex.ImageState.BODY_G_F);
				body[i].filling();
			}
			else
				body[i].setImageState(Vertex.ImageState.BODY_G_E);
		}
		for (int i = MAX_R+MAX_G+1; i < MAX_R+MAX_G+MAX_B+1; i++) {
			body[i] = new Vertex(0,0);
			if (i<MAX_R+MAX_G+B+1)
			{
				body[i].setImageState(Vertex.ImageState.BODY_B_F);
				body[i].filling();
			}

			else
				body[i].setImageState(Vertex.ImageState.BODY_B_E);
		}
	}

	//蛇移动方法
	void move() {
		for (int i = length-1; i > 0; i--) {
			body[i].setLocation(body[i-1]);
		}
		if (direction.equals(Direciton.RIGHT)) {
			int newX = (body[0].getX() == boundX_1) ? boundX_2 : body[0].getX() + 32;
			body[0].setX(newX);
		}
		if (direction.equals(Direciton.LEFT)) {
			int newX = (body[0].getX() == boundX_2) ? boundX_1 : body[0].getX() - 32;
			body[0].setX(newX);
		}
		if (direction.equals(Direciton.UP)) {
			int newY = (body[0].getY() == boundY_2) ? boundY_1 : body[0].getY() - 32;
			body[0].setY(newY);
		}
		if (direction.equals(Direciton.DOWN)) {
			int newY = (body[0].getY() == boundY_1) ? boundY_2 : body[0].getY() + 32;
			body[0].setY(newY);
		}


	}

	void setDirection(Direciton newDireciton){
		this.direction = newDireciton;
	}

	// Inside the Snake class
	public boolean headCollidesWithBody(Snake otherSnake) {
		for (int i = 1; i < otherSnake.body.length; i++) {
			if (this.body[0].equals(otherSnake.body[i])) {
				return true; // Collision detected
			}
		}
		return false; // No collision
	}

	public void updateAllocation(Food food) {
		boolean foundMatch = false;

		for (int i = 1; i < length && !foundMatch; i++) {
			if (!body[i].isFilled) {
				Vertex.ImageState imageState = body[i].imageState;

				switch (imageState) {
					case BODY_R_E:
						if (food.getImageState().equals(Food.foodImageState.RED)) {
							body[i].filling();
							foundMatch = true;
						}
						break;
					case BODY_G_E:
						if (food.getImageState().equals(Food.foodImageState.GREEN)) {
							body[i].filling();
							foundMatch = true;
						}
						break;
					case BODY_B_E:
						if (food.getImageState().equals(Food.foodImageState.BLUE)) {
							body[i].filling();
							foundMatch = true;
						}
						break;
				}
			}
		}
	}

	public boolean checkComplete() {
		for (int i = 0; i < length; i++) {
			if (!body[i].isFilled) {
				return false; // 有未填充的部分，返回false
			}
		}
		this.isCompleted = true;
		return true; // 所有部分都已填充
	}

	public boolean getIsCompleted(){
		return isCompleted;
	}

	public void setLocation(int x, int y){
		body[0].setLocation(x,y);
		body[0].setImageState(Vertex.ImageState.HEAD_U);
		this.direction = Direciton.UP;
		for (int i = 1; i < body.length; i++) {
			body[i].setLocation(x,y+32*i);
		}
	}

	public void setBound(int bX_1, int bX_2, int bY_1, int bY_2){
		this.boundX_1 = bX_1;
		this.boundX_2 = bX_2;
		this.boundY_1 = bY_1;
		this.boundY_2 = bY_2;
	}

	public int[] returnNeed(){
		int[] need  = new int[3];
		for (Vertex element :body) {
			if (!element.isFilled){
				Vertex.ImageState imageState = element.imageState;
				switch (imageState) {
					case BODY_R_E:
						need[0]++;
						break;
					case BODY_G_E:
						need[1]++;
						break;
					case BODY_B_E:
						need[2]++;
				}
			}
		}
		return need;
	}
	public int[] returnMAX(){
		int[] max = new int[3];
		max[0] = MAX_R;
		max[1] = MAX_G;
		max[2] = MAX_B;
		return max;
	}

	//检查当前状态能否再吃传入类型的食物
	public boolean checkEat(Food food){
		for (int i = 1; i < length; i++) {
			Vertex.ImageState imageState = body[i].imageState;
				switch (imageState) {
					case BODY_R_E:
						if (food.getImageState().equals(Food.foodImageState.RED)) {
							return true;
						}
						break;
					case BODY_G_E:
						if (food.getImageState().equals(Food.foodImageState.GREEN)) {
							return true;
						}
						break;
					case BODY_B_E:
						if (food.getImageState().equals(Food.foodImageState.BLUE)) {
							return true;
						}
						break;
				}
			}
		return false;
		}
}