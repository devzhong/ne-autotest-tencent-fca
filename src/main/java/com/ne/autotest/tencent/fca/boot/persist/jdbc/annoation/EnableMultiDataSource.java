package com.ne.autotest.tencent.fca.boot.persist.jdbc.annoation;

import com.ne.autotest.tencent.fca.boot.persist.jdbc.MultiDataSourceRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({MultiDataSourceRegister.class})
public @interface EnableMultiDataSource {
    String[] keys();

    String defaultKey();
}

