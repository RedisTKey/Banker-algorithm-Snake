import javax.swing.*;

public class Vertex {
    int xR1,yR1;            //坐标
    ImageState imageState;  // 图片状态

    boolean isFilled = false;

    // 定义可能的图像状态
    enum ImageState {
        BODY_R_F, BODY_R_E, BODY_B_F, BODY_B_E, BODY_G_F, BODY_G_E,
        HEAD_R, HEAD_L, HEAD_U, HEAD_D
    }


    /**======================= 所有Vertex可能对应的图片 ===========================*/

    ImageIcon body_R_F = new ImageIcon("src/Images/Snake_Body_R_F.png");
    ImageIcon body_R_E = new ImageIcon("src/Images/Snake_Body_R_E.png");
    ImageIcon body_B_F = new ImageIcon("src/Images/Snake_Body_B_F.png");
    ImageIcon body_B_E = new ImageIcon("src/Images/Snake_Body_B_E.png");
    ImageIcon body_G_F = new ImageIcon("src/Images/Snake_Body_G_F.png");
    ImageIcon body_G_E = new ImageIcon("src/Images/Snake_Body_G_E.png");
    ImageIcon head_R = new ImageIcon("src/Images/Snake_Head_R.png");
    ImageIcon head_L = new ImageIcon("src/Images/Snake_Head_L.png");
    ImageIcon head_U = new ImageIcon("src/Images/Snake_Head_U.png");
    ImageIcon head_D = new ImageIcon("src/Images/Snake_Head_D.png");



    public Vertex(int x, int y)
    {
        xR1 = x;
        yR1 = y;
        imageState = ImageState.BODY_R_E;
    }
    public boolean equals(Vertex vertex) {
        if (vertex.xR1 == xR1 && vertex.yR1 == yR1)
            return true;
        return false;
    }

    //更新坐标
    public void setLocation(Vertex newVertex){
        this.setX(newVertex.getX());
        this.setY(newVertex.getY());
    }

    public void setLocation(int x, int y){
        this.setX(x);
        this.setY(y);
    }

    public int getX() {
        return xR1;
    }
    public void setX(int x){xR1 = x;}

    public int getY() {
        return yR1;
    }
    public void setY(int y){yR1 = y;}
    public ImageIcon getImage() {
        switch (imageState) {
            case BODY_R_F:
                return body_R_F;
            case BODY_R_E:
                return body_R_E;
            case BODY_B_F:
                return body_B_F;
            case BODY_B_E:
                return body_B_E;
            case BODY_G_F:
                return body_G_F;
            case BODY_G_E:
                return body_G_E;
            case HEAD_R:
                return head_R;
            case HEAD_L:
                return head_L;
            case HEAD_U:
                return head_U;
            case HEAD_D:
                return head_D;
            default:
                // Handle the default case, for example, return a default image or throw an exception
                return null;
        }
    }

    public ImageState getImageState() {
        return imageState;
    }

    public void setImageState(ImageState newImageState) {
        this.imageState = newImageState;
    }
    public void filling()
    {
        isFilled = true;
        switch (imageState) {
            case BODY_R_E:
                imageState = ImageState.BODY_R_F;
                break;
            case BODY_B_E:
                imageState = ImageState.BODY_B_F;
                break;
            case BODY_G_E:
                imageState = ImageState.BODY_G_F;
            case HEAD_R:
                break;
        }
    }
}
