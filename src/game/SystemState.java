package game;

/**
 * Created by alexanderm on 28/02/2018.
 */
public class SystemState {
    private Color verticalLight;
    private Color horizontalLight;

    public SystemState(Color verticalLight, Color horizontalLight) {
        this.verticalLight = verticalLight;
        this.horizontalLight = horizontalLight;
    }
    public Color getVerticalLight() {
        return verticalLight;
    }

    public void setVerticalLight(Color verticalLight) {
        this.verticalLight = verticalLight;
    }

    public Color getHorizontalLight() {
        return horizontalLight;
    }

    public void setHorizontalLight(Color horizontalLight) {
        this.horizontalLight = horizontalLight;
    }


}
