package edu.hm.smartpower.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 130210708;

    public static final QUser user = new QUser("user");

    public final NumberPath<Integer> gramPerKwh = createNumber("gramPerKwh", Integer.class);

    public final DateTimePath<java.util.Date> lastDeviationNotification = createDateTime("lastDeviationNotification", java.util.Date.class);

    public final DateTimePath<java.util.Date> lastMaxUsageNotification = createDateTime("lastMaxUsageNotification", java.util.Date.class);

    public final NumberPath<Integer> maxDeviationFromAverage = createNumber("maxDeviationFromAverage", Integer.class);

    public final NumberPath<Integer> maxUsagePerDay = createNumber("maxUsagePerDay", Integer.class);

    public final BooleanPath notificationsActivated = createBoolean("notificationsActivated");

    public final StringPath password = createString("password");

    public final NumberPath<Integer> personsInHousehold = createNumber("personsInHousehold", Integer.class);

    public final NumberPath<java.math.BigDecimal> pricePerKwh = createNumber("pricePerKwh", java.math.BigDecimal.class);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QUser(Path<? extends User> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata<?> metadata) {
        super(User.class, metadata);
    }

}

