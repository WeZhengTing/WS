package com.zc.common;

import com.jfinal.config.*;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.zc.model._MappingKit;

public class CommonConfig extends JFinalConfig {
    public  static Prop p;
    public static DruidPlugin createDruidPlugin() {
        loadConfig();

        return new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password"));
    }

    private static void loadConfig() {
        if (p == null) {
            p = PropKit.useFirstFound("config.txt");
        }
    }

    @Override
    public void configConstant(Constants constants) {
        loadConfig();
        constants.setDevMode(true);
        constants.setEncoding("UTF8");
        constants.setMaxPostSize(1024*1024*1024);
        constants.setBaseUploadPath(p.get("baseUploadPath"));
    }

    @Override
    public void configRoute(Routes routes) {
                routes.scan("com.zc.controller");
    }

    @Override
    public void configEngine(Engine engine) {

    }

    @Override
    public void configPlugin(Plugins plugins) {
        // 配置 druid 数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password"));
        plugins.add(druidPlugin);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        // 所有映射在 MappingKit 中自动化搞定
        _MappingKit.mapping(arp);
        plugins.add(arp);
    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    @Override
    public void configHandler(Handlers handlers) {

    }
}
