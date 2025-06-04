package model;

public class ConfiguracionApp {
    private boolean bluetoothActivado;
    private String estadoConexionBluetooth;

    public ConfiguracionApp() {
        this.bluetoothActivado = false;
        this.estadoConexionBluetooth = "No conectado";
    }

    public boolean isBluetoothActivado() {
        return bluetoothActivado;
    }

    public void setBluetoothActivado(boolean bluetoothActivado) {
        this.bluetoothActivado = bluetoothActivado;
    }

    public String getEstadoConexionBluetooth() {
        return estadoConexionBluetooth;
    }

    public void setEstadoConexionBluetooth(String estadoConexionBluetooth) {
        this.estadoConexionBluetooth = estadoConexionBluetooth;
    }

    @Override
    public String toString() {
        return "ConfiguracionApp{" +
               "bluetoothActivado=" + bluetoothActivado +
               ", estadoConexionBluetooth='" + estadoConexionBluetooth + '\'' +
               '}';
    }
}