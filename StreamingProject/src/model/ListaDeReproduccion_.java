package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-10-05T10:37:27.296-0500")
@StaticMetamodel(ListaDeReproduccion.class)
public class ListaDeReproduccion_ {
	public static volatile SingularAttribute<ListaDeReproduccion, Long> id;
	public static volatile SingularAttribute<ListaDeReproduccion, String> name;
	public static volatile SingularAttribute<ListaDeReproduccion, Integer> duration;
	public static volatile SingularAttribute<ListaDeReproduccion, Usuario> user;
	public static volatile ListAttribute<ListaDeReproduccion, Video> videos;
}
