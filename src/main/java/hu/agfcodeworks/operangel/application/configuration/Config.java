package hu.agfcodeworks.operangel.application.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("hu.agfcodeworks.operangel.application")
@PropertySource("classpath:application.properties")
@Configuration
public class Config {

}
