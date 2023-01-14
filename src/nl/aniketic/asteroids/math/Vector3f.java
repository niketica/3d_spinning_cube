package nl.aniketic.asteroids.math;

public class Vector3f {

    public float x;
    public float y;
    public float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f mul(float value) {
        float x = this.x * value;
        float y = this.y * value;
        float z = this.z * value;

        return new Vector3f(
                x,
                y,
                z
        );
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "," + z + "]";
    }


}
