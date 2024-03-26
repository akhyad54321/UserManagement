/**
 * 
 */
package sa.tabadul.useraccessmanagement.constant;

/**
 * @author amalkawi
 *
 */
public enum PortsType {
    SEA(100), DRY(101), LAND(102);

    private final int portsType;

    PortsType(int portsType) {
        this.portsType = portsType;
    }

    public int getPortsType() {
        return portsType;
    }

    public static PortsType getByPortsType(String portsType) {
        for (PortsType portsTypeObj : values()) {
            if (portsTypeObj.name().equalsIgnoreCase(portsType)) {
                return portsTypeObj;
            }
        }
        return SEA;
    }
}
