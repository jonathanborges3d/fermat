package com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_submit_offline_merchandise.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededPluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;

import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.CantInitializeDatabaseException;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_purchase.interfaces.CustomerBrokerContractPurchaseManager;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_sale.interfaces.CustomerBrokerContractSaleManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiationManager;
import com.bitdubai.fermat_cbp_api.layer.network_service.transaction_transmission.interfaces.TransactionTransmissionManager;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_submit_offline_merchandise.developer.bitdubai.version_1.database.BrokerSubmitOfflineMerchandiseBusinessTransactionDao;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_submit_offline_merchandise.developer.bitdubai.version_1.database.BrokerSubmitOfflineMerchandiseBusinessTransactionDatabaseConstants;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_submit_offline_merchandise.developer.bitdubai.version_1.database.BrokerSubmitOfflineMerchandiseBusinessTransactionDatabaseFactory;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_submit_offline_merchandise.developer.bitdubai.version_1.database.BrokerSubmitOfflineMerchandiseBusinessTransactionDeveloperDatabaseFactory;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_submit_offline_merchandise.developer.bitdubai.version_1.event_handler.BrokerSubmitOfflineMerchandiseRecorderService;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_submit_offline_merchandise.developer.bitdubai.version_1.structure.BrokerSubmitOfflineMerchandiseTransactionManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Manuel Perez on 21/12/2015
 */

public class BrokerSubmitOfflineMerchandisePluginRoot extends AbstractPlugin implements
        DatabaseManagerForDevelopers,
        LogManagerForDevelopers {

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    ErrorManager errorManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    private EventManager eventManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.LOG_MANAGER)
    LogManager logManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_DATABASE_SYSTEM)
    private PluginDatabaseSystem pluginDatabaseSystem;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.NETWORK_SERVICE, plugin = Plugins.TRANSACTION_TRANSMISSION)
    private TransactionTransmissionManager transactionTransmissionManager;

    //TODO: Need reference to contract plugin
    private CustomerBrokerContractPurchaseManager customerBrokerContractPurchaseManager;

    //TODO: Need reference to contract plugin
    private CustomerBrokerContractSaleManager customerBrokerContractSaleManager;

    //TODO: Need reference to contract plugin
    private CustomerBrokerSaleNegotiationManager customerBrokerSaleNegotiationManager;

    /**
     * Represents the plugin manager.
     */
    BrokerSubmitOfflineMerchandiseTransactionManager brokerSubmitOfflineMerchandiseTransactionManager;

    /**
     * Represents the plugin BrokerSubmitOfflineMerchandiseBusinessTransactionDatabaseFactory
     */
    BrokerSubmitOfflineMerchandiseBusinessTransactionDeveloperDatabaseFactory brokerSubmitOfflineMerchandiseBusinessTransactionDeveloperDatabaseFactory;

    /**
     * Represents the database
     */
    Database database;

    static Map<String, LogLevel> newLoggingLevel = new HashMap<String, LogLevel>();

    public BrokerSubmitOfflineMerchandisePluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<String>();
        returnedClasses.add("com.bitdubai.fermat_cbp_plugin.layer.business_transaction.broker_submit_offline_merchandise.developer.bitdubai.version_1.BrokerSubmitOfflineMerchandisePluginRoot");
        return returnedClasses;
    }

    /**
     * This method initialize the database
     *
     * @throws CantInitializeDatabaseException
     */
    private void initializeDb() throws CantInitializeDatabaseException {

        try {
            /*
             * Open new database connection
             */
            this.database = this.pluginDatabaseSystem.openDatabase(
                    pluginId,
                    BrokerSubmitOfflineMerchandiseBusinessTransactionDatabaseConstants.DATABASE_NAME);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

            /*
             * The database exists but cannot be open. I can not handle this situation.
             */
            errorManager.reportUnexpectedPluginException(
                    Plugins.BROKER_SUBMIT_OFFLINE_MERCHANDISE,
                    UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                    cantOpenDatabaseException);
            throw new CantInitializeDatabaseException(cantOpenDatabaseException.getLocalizedMessage());

        } catch (DatabaseNotFoundException e) {

            /*
             * The database no exist may be the first time the plugin is running on this device,
             * We need to create the new database
             */
            BrokerSubmitOfflineMerchandiseBusinessTransactionDatabaseFactory brokerSubmitOfflineMerchandiseBusinessTransactionDatabaseFactory =
                    new BrokerSubmitOfflineMerchandiseBusinessTransactionDatabaseFactory(pluginDatabaseSystem);

            try {

                /*
                 * We create the new database
                 */
                this.database = brokerSubmitOfflineMerchandiseBusinessTransactionDatabaseFactory.createDatabase(
                        pluginId,
                        BrokerSubmitOfflineMerchandiseBusinessTransactionDatabaseConstants.DATABASE_NAME);

            } catch (CantCreateDatabaseException cantOpenDatabaseException) {

                /*
                 * The database cannot be created. I can not handle this situation.
                 */
                errorManager.reportUnexpectedPluginException(
                        Plugins.BROKER_SUBMIT_OFFLINE_MERCHANDISE,
                        UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                        cantOpenDatabaseException);
                throw new CantInitializeDatabaseException(cantOpenDatabaseException.getLocalizedMessage());

            }
        }

    }

    @Override
    public void setLoggingLevelPerClass(Map<String, LogLevel> newLoggingLevel) {
        try {
            for (Map.Entry<String, LogLevel> pluginPair : newLoggingLevel.entrySet()) {
                if (BrokerSubmitOfflineMerchandisePluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                    BrokerSubmitOfflineMerchandisePluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                    BrokerSubmitOfflineMerchandisePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
                } else {
                    BrokerSubmitOfflineMerchandisePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
                }
            }
        } catch (Exception exception) {
            this.errorManager.reportUnexpectedPluginException(
                    Plugins.BROKER_SUBMIT_OFFLINE_MERCHANDISE,
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    exception);
        }
    }

    ServiceStatus serviceStatus = ServiceStatus.CREATED;


    @Override
    public void start() throws CantStartPluginException {
        try {

            /**
             * Initialize database
             */
            initializeDb();

            /*
             * Initialize Developer Database Factory
             */
            brokerSubmitOfflineMerchandiseBusinessTransactionDeveloperDatabaseFactory = new
                    BrokerSubmitOfflineMerchandiseBusinessTransactionDeveloperDatabaseFactory(pluginDatabaseSystem,
                    pluginId);
            brokerSubmitOfflineMerchandiseBusinessTransactionDeveloperDatabaseFactory.initializeDatabase();

            /**
             * Initialize Dao
             */
            BrokerSubmitOfflineMerchandiseBusinessTransactionDao brokerSubmitOfflineMerchandiseBusinessTransactionDao=
                    new BrokerSubmitOfflineMerchandiseBusinessTransactionDao(pluginDatabaseSystem,
                            pluginId,
                            database);

            /**
             * Init the plugin manager
             */
            this.brokerSubmitOfflineMerchandiseTransactionManager=new BrokerSubmitOfflineMerchandiseTransactionManager(
                    brokerSubmitOfflineMerchandiseBusinessTransactionDao,
                    this.customerBrokerContractSaleManager,
                    this.customerBrokerSaleNegotiationManager
            );

            /**
             * Init event recorder service.
             */
            BrokerSubmitOfflineMerchandiseRecorderService brokerSubmitOfflineMerchandiseRecorderService=
                    new BrokerSubmitOfflineMerchandiseRecorderService(
                            brokerSubmitOfflineMerchandiseBusinessTransactionDao,
                            eventManager);
            brokerSubmitOfflineMerchandiseRecorderService.start();

            /**
             * Init monitor Agent
             */
            /*BrokerSubmitOnlineMerchandiseMonitorAgent brokerSubmitOnlineMerchandiseMonitorAgent=new BrokerSubmitOnlineMerchandiseMonitorAgent(
                    pluginDatabaseSystem,
                    logManager,
                    errorManager,
                    eventManager,
                    pluginId,
                    transactionTransmissionManager,
                    customerBrokerContractPurchaseManager,
                    customerBrokerContractSaleManager,
                    outgoingIntraActorManager,
                    cryptoMoneyDeStockManager);
            brokerSubmitOnlineMerchandiseMonitorAgent.start();*/

            this.serviceStatus = ServiceStatus.STARTED;
            //System.out.println("Broker submit offline merchandise starting");
        } catch (Exception exception) {
            //TODO: handle correctly this method exceptions
            throw new CantStartPluginException(
                    CantStartPluginException.DEFAULT_MESSAGE,
                    FermatException.wrapException(exception),
                    null,
                    null);
        }
    }

    @Override
    public void pause() {
        this.serviceStatus = ServiceStatus.PAUSED;
    }

    @Override
    public void resume() {
        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void stop() {
        this.serviceStatus = ServiceStatus.STOPPED;
    }

    @Override
    public FermatManager getManager() {
        return this.brokerSubmitOfflineMerchandiseTransactionManager;
    }

    public static LogLevel getLogLevelByClass(String className) {
        try{
            /**
             * sometimes the classname may be passed dynamically with an $moretext
             * I need to ignore whats after this.
             */
            String[] correctedClass = className.split((Pattern.quote("$")));
            return BrokerSubmitOfflineMerchandisePluginRoot.newLoggingLevel.get(correctedClass[0]);
        } catch (Exception e){
            /**
             * If I couldn't get the correct logging level, then I will set it to minimal.
             */
            return DEFAULT_LOG_LEVEL;
        }
    }


    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        return brokerSubmitOfflineMerchandiseBusinessTransactionDeveloperDatabaseFactory.getDatabaseList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        return brokerSubmitOfflineMerchandiseBusinessTransactionDeveloperDatabaseFactory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        return brokerSubmitOfflineMerchandiseBusinessTransactionDeveloperDatabaseFactory.getDatabaseTableContent(developerObjectFactory, developerDatabaseTable);
    }
}