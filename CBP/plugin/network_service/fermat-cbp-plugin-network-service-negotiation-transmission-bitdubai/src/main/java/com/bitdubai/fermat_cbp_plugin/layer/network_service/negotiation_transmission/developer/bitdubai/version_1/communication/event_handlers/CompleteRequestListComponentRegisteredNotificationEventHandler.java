/*
 * @#CompleteRequestListComponentRegisteredNotificationEventHandler.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_cbp_plugin.layer.network_service.negotiation_transmission.developer.bitdubai.version_1.communication.event_handlers;

import com.bitdubai.fermat_api.layer.all_definition.network_service.interfaces.NetworkService;
import com.bitdubai.fermat_p2p_api.layer.all_definition.common.network_services.template.event_handlers.AbstractCommunicationBaseEventHandler;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.events.CompleteRequestListComponentRegisteredNotificationEvent;

/**
 * The Class <code>com.bitdubai.fermat_dmp_plugin.layer.network_service.template.developer.bitdubai.version_1.event_handlers.CompleteRequestListComponentRegisteredNotificationEventHandler</code>
 * implements the handle to the event <code>com.bitdubai.fermat_api.layer.platform_service.event_manager.events.CompleteRequestListComponentRegisteredNotificationEvent</code><p/>
 *
 * Created by Roberto Requena - (rart3001@gmail.com) on 22/09/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CompleteRequestListComponentRegisteredNotificationEventHandler extends AbstractCommunicationBaseEventHandler<CompleteRequestListComponentRegisteredNotificationEvent>  {

    /**
     * Constructor with parameter
     *
     * @param networkService
     */
    public CompleteRequestListComponentRegisteredNotificationEventHandler(NetworkService networkService) {
        super(networkService);
    }


    @Override
    public void processEvent(CompleteRequestListComponentRegisteredNotificationEvent event) {
        this.networkService.handleCompleteRequestListComponentRegisteredNotificationEvent(event.getRegisteredComponentList());
    }
}
