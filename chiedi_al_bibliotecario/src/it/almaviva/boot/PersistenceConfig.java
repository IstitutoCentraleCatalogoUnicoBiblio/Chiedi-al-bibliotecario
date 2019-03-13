/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.almaviva.boot;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import it.almaviva.boot.appVersion.Version;
import it.almaviva.boot.appVersion.VersioningReader;
import it.almaviva.cms.utilities.PropertiesLoader;
import it.almaviva.cms.utilities.costanti.Constants;



//Classe di configurazione Database occhio sempre al package
@Configuration
@EnableTransactionManagement
@ComponentScan({
	Constants.pkg_spring_scanner_db
	})
public class PersistenceConfig {
	
	private Logger log = Logger.getLogger(PersistenceConfig.class);

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		return em;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		PropertiesLoader pl = new PropertiesLoader();
		Version version = VersioningReader.getVersion();
		log.info("-------------------------------------------------------------------------------");
		log.info("------------------------------- STARTING --------------------------------------");

			log.info("-->Versione: " + version.getVersion());
			log.info("-->Release date: " +  version.getLast_release());
			log.info("-------------------------------------------------------------------------------");
			
			log.info("lettura conf. db da file");
			
			ds.setDriverClassName(pl.getProps("DB_DRIVER_CLASS"));
			ds.setUrl(pl.getProps("DB_URL"));
			ds.setUsername(pl.getProps("DB_USER"));
			ds.setPassword(pl.getProps("DB_PASSWORD"));
			ds.setSchema(pl.getProps("DB_SCHEMA"));
		
		return ds;
	}

	@Bean
	public PlatformTransactionManager txManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

}
