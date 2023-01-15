package nl.aniketic.spinningcube.math;

public class Vector3f {

    public float x;
    public float y;
    public float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f add(Vector3f v) {
        float x = this.x + v.x;
        float y = this.y + v.y;
        float z = this.z + v.z;

        return new Vector3f(
                x,
                y,
                z
        );
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

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3f normalize() {
        float l = length();
        float x = this.x / l;
        float y = this.y / l;
        float z = this.z / l;
        return new Vector3f(
                x, y, z
        );
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "," + z + "]";
    }


}
