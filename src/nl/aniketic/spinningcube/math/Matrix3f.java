package nl.aniketic.spinningcube.math;

public class Matrix3f {
    
    public float 
            m00, m01, m02,
            m10, m11, m12,
            m20, m21, m22;
    
    public Matrix3f() {
        setIdentity();
    }
    
    public void setIdentity() {
        Matrix3f.setIdentity(this);
    }
    
    public static void setIdentity(Matrix3f m) {
        m.m00 = 1.0f;
        m.m01 = 0.0f;
        m.m02 = 0.0f;

        m.m10 = 0.0f;
        m.m11 = 1.0f;
        m.m12 = 0.0f;

        m.m20 = 0.0f;
        m.m21 = 0.0f;
        m.m22 = 1.0f;
    }
    
    public Vector3f transform(Vector3f v) {
        return Matrix3f.transform(this, v);
    }
    
    public static Vector3f transform(Matrix3f m, Vector3f v) {
        float x = m.m00 * v.x + m.m10 * v.y + m.m20 * v.z;
        float y = m.m01 * v.x + m.m11 * v.y + m.m21 * v.z;
        float z = m.m02 * v.x + m.m12 * v.y + m.m22 * v.z;
        
        return new Vector3f(
                x,
                y,
                z
        );
    }

    public Vector3f mul(Vector3f v) {
        return Matrix3f.mul(this, v);
    }

    public static Vector3f mul(Matrix3f m, Vector3f v) {
        float x = m.m00 * v.x + m.m01 * v.y + m.m02 * v.z;
        float y = m.m10 * v.x + m.m11 * v.y + m.m12 * v.z;
        float z = m.m20 * v.x + m.m21 * v.y + m.m22 * v.z;

        return new Vector3f(
                x,
                y,
                z
        );
    }
}
