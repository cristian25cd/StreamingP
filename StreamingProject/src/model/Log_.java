package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-10-05T10:37:28.230-0500")
@StaticMetamodel(Log.class)
public class Log_ {
	public static volatile SingularAttribute<Log, Integer> id;
	public static volatile SingularAttribute<Log, Date> created;
	public static volatile SingularAttribute<Log, Usuario> user;
	public static volatile SingularAttribute<Log, String> ip;
}
