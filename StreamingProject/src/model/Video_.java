package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-10-05T10:37:28.234-0500")
@StaticMetamodel(Video.class)
public class Video_ {
	public static volatile SingularAttribute<Video, Long> id;
	public static volatile SingularAttribute<Video, String> name;
	public static volatile SingularAttribute<Video, String> url;
	public static volatile SingularAttribute<Video, Integer> duration;
	public static volatile SingularAttribute<Video, String> format;
	public static volatile SingularAttribute<Video, Usuario> user;
	public static volatile SingularAttribute<Video, Date> add_date;
}
