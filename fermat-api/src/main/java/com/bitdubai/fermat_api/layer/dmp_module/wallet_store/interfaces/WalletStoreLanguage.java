package com.bitdubai.fermat_api.layer.dmp_module.wallet_store.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.enums.Languages;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_store.enums.InstallationStatus;
import com.bitdubai.fermat_api.layer.dmp_network_service.wallet_store.interfaces.Language;

import java.util.UUID;

/**
 * Created by eze on 2015.07.18..
 */
public interface WalletStoreLanguage extends Language{

    public InstallationStatus getInstallationStatus();
    public String getTranslatorName();
    public String getTranslatorPublicKey();
}