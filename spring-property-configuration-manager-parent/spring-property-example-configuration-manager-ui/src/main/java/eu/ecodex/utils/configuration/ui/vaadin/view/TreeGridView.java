/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.configuration.ui.vaadin.view;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import eu.ecodex.utils.configuration.domain.ConfigurationPropertyNode;
import eu.ecodex.utils.configuration.service.ConfigurationPropertyCollector;
import java.util.Properties;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@HtmlImport("styles/shared-styles.html")
@Route(value = "treegridview", layout = MainView.class)
@PageTitle("Spring Properties Configuration Manager")
public class TreeGridView extends VerticalLayout {

    private static final Logger LOGGER = LogManager.getLogger(TreeGridView.class);

    TreeGrid<ConfigurationPropertyNode> grid = new TreeGrid<>();

    @Autowired
    ConfigurationPropertyCollector configurationPropertyCollector;

    Properties properties = new Properties();

    public TreeGridView() {
        this.add(this.grid);

    }

    @PostConstruct
    public void init() {
//        Collection<ConfigurationProperty> all = configurationPropertyCollector.getConfigurationProperties("eu.ecodex.utils.configuration.example1");
        ConfigurationPropertyNode configurationPropertiesHirachie =
                configurationPropertyCollector.getConfigurationPropertiesHirachie(
                        "eu.ecodex.utils.configuration.example1");

//        LOGGER.debug("tree view is {}", configurationPropertiesHirachie);

//        grid.setItems(all);

        HierarchicalDataProvider dataProvider =
                new AbstractBackEndHierarchicalDataProvider<ConfigurationPropertyNode, Void>() {

                    @Override
                    public int getChildCount(
                            HierarchicalQuery<ConfigurationPropertyNode, Void> query) {
                        if (query.getParent() == null) {
                            return configurationPropertiesHirachie.getChildren().size();
                        }
                        return query.getParent().getChildren().size();
                    }

                    @Override
                    public boolean hasChildren(ConfigurationPropertyNode item) {
                        return item.getChildren().size() != 0;
                    }

                    @Override
                    protected Stream<ConfigurationPropertyNode> fetchChildrenFromBackEnd(
                            HierarchicalQuery<ConfigurationPropertyNode, Void> query) {
                        if (query.getParent() == null) {
                            return configurationPropertiesHirachie.getChildren().stream();
                        }
                        return query.getParent().getChildren().stream();
                    }
                };

        grid.setDataProvider(dataProvider);


        grid.addHierarchyColumn(ConfigurationPropertyNode::getNodeName).setHeader("Node Name");
        grid.addColumn(new ValueProvider<ConfigurationPropertyNode, String>() {
            @Override
            public String apply(ConfigurationPropertyNode propertyNode) {
                if (propertyNode.getProperty() != null) {
                    return propertyNode.getProperty().getLabel();
                }
                return null;
            }
        }).setHeader("Property Label");


//        List<Grid.Column<ConfigurationPropertyNode>> columns = grid.getColumns();
//        columns.remove(property_name);
//        columns.add(0, property_name);
//        grid.setColumnOrder(columns);


//        grid.addColumn("property").setHeader("");

//        grid.addColumn("property.label").setHeader("Human Name");

//        grid.setColumns("propertyName", "description", "label");

    }


}
