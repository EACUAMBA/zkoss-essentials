package com.mafurrasoft.dev.cap_03.controller;

import com.mafurrasoft.dev.cap_03.service.AuthenticationServiceCap_03Impl;
import com.mafurrasoft.dev.cap_03.service.UserInfoServiceCap_03Impl;
import com.mafurrasoft.dev.entity.User;
import com.mafurrasoft.dev.services.AuthenticationService;
import com.mafurrasoft.dev.services.CommonInfoService;
import com.mafurrasoft.dev.services.UserCredential;
import com.mafurrasoft.dev.services.UserInfoService;
import com.sun.security.ntlm.Client;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.sql.SQLOutput;
import java.util.Set;

public class ProfileViewController extends SelectorComposer<Component> {

    @Wire
    Label account;

    @Wire
    Textbox fullName;

    @Wire
    Textbox email;

    @Wire
    Datebox birthday;

    @Wire
    Listbox country;

    @Wire
    Textbox bio;

    @Wire
    Label namelabel;


    AuthenticationService authenticationService = new AuthenticationServiceCap_03Impl();
    UserInfoService userInfoService = new UserInfoServiceCap_03Impl();

    @Override
    public void doAfterCompose(Component component) throws Exception {
        super.doAfterCompose(component);

        ListModelList<String> countryModel = new ListModelList<String>(CommonInfoService.getCountryList());
        country.setModel(countryModel);
    }

    private void refreshProfileView() {
        UserCredential userCredential = authenticationService.getUserCredential();
        User user = userInfoService.findUser(userCredential.getAccount());

        if (user == null)
            return;

        account.setValue(user.getAccount());
        fullName.setValue(user.getFullName());
        this.namelabel.setValue(this.fullName.getValue());
        email.setValue(user.getEmail());
        birthday.setValue(user.getBirthday());
        bio.setValue(user.getBio());

        ((ListModelList<String>) country.<String>getModel()).addToSelection(user.getCountry());
    }


    //Salvando dados
    @Listen("onClick=#saveProfile")
    public void doSaveProfile() {
        UserCredential userCredential = authenticationService.getUserCredential();
        User user = userInfoService.findUser(userCredential.getAccount());

        if (user == null)
            return;

        user.setFullName(this.fullName.getValue());
        user.setEmail(this.email.getValue());
        user.setBirthday(this.birthday.getValue());
        user.setBio(this.bio.getValue());

        Set<String> selection = ((ListModelList<String>) this.country.<String>getModel()).getSelection();
        if (!selection.isEmpty()) {
            user.setCountry(selection.iterator().next());
        } else {
            user.setCountry(null);
        }

        userInfoService.updateUser(user);
        Clients.showNotification("O teu perfil foi actulizado com sucesso.");
    }

    @Listen("onClick=#reloadProfile")
    public void doReloadProfile() {
        this.refreshProfileView();
    }
}
