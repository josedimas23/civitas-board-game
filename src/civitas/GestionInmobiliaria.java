package civitas;

public class GestionInmobiliaria {
    
    private OperacionInmobiliaria operacion;
    private int propiedad;
    
    public GestionInmobiliaria(OperacionInmobiliaria oper , int prop){
        
        operacion = oper;
        propiedad = prop;
    }
    
    OperacionInmobiliaria getOperacion(){
        return operacion;
    }
    
    int getPropiedad(){
        return propiedad;
    }
    
}
