/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: MailManager.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10  2015/09/01 19:01:46  fisherma
 *  Archive Log:    PR 130111 - test button unavalable after entering SMTP information.  The fix is to allow to save properties regardless of their validity.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/08/21 20:45:49  jijunwan
 *  Archive Log:    PR 128974 - Email notification functionality.
 *  Archive Log:    - improved to be silent on errors when this feature is turned off
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/08/21 12:32:29  robertja
 *  Archive Log:    PR 128974 - Delay MailManager notifications to the front end until there is a registered listener.
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/08/21 03:54:21  fisherma
 *  Archive Log:    Added property to turn email notifications feature on/off.
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/08/19 19:25:28  fernande
 *  Archive Log:    PR 128703 - Fail over doesn't work on A0 Fabric. FE Adapter not being shutdown during application shutdown
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2015/08/17 18:48:56  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/08/14 19:46:32  fisherma
 *  Archive Log:    Allow user to disable email notifications by leaving the SMTP server name field empty.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/08/10 17:04:52  robertja
 *  Archive Log:    PR128974 - Email notification functionality.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/04/22 16:51:39  fernande
 *  Archive Log:    Reorganized the startup sequence so that the UI plugin could initialize its own CertsAssistant. This way, autoconnect subnets would require a password using the UI CertsAssistant instead of the default CertsAssistant.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2015/02/04 21:29:36  jijunwan
 *  Archive Log:    added Mail Manager
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.api.configuration.impl;

import static com.intel.stl.common.STLMessages.STL10019_MAIL_COMPONENT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.configuration.AppInfo;
import com.intel.stl.api.configuration.MailProperties;
import com.intel.stl.api.notice.GenericNoticeAttrBean;
import com.intel.stl.api.notice.IEmailEventListener;
import com.intel.stl.api.notice.NoticeBean;
import com.intel.stl.api.notice.NoticeType;
import com.intel.stl.api.notice.TrapType;
import com.intel.stl.api.subnet.GIDGlobal;
import com.intel.stl.common.STLMessages;
import com.intel.stl.configuration.AppComponent;
import com.intel.stl.configuration.AppConfigurationException;
import com.intel.stl.configuration.AppSettings;
import com.intel.stl.datamanager.DatabaseManager;

public class MailManager implements AppComponent {
    private static Logger log = LoggerFactory.getLogger(MailManager.class);

    // SMTP properties strings.
    private final String SMTP_FROM_ADDRESS = "smtp_from_address";

    private final String SMTP_PORT = "smtp_port";

    private final String SMTP_SERVER_NAME = "smtp_server_name";

    private final String SMTP_NOTIFICATIONS_ENABLED =
            "smtp_notifications_enabled";

    private MailSender mailSender;

    private MailProperties mailProperties;

    private final DatabaseManager databaseManager;

    private final List<IEmailEventListener<NoticeBean>> emailEventListeners =
            new CopyOnWriteArrayList<IEmailEventListener<NoticeBean>>();

    private boolean isSmtpValid = false;

    private final List<NoticeBean> toSend = new ArrayList<NoticeBean>();

    public MailManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.configuration.AppComponent#initialize(com.intel.stl.
     * configuration.AppSettings)
     */
    @Override
    public void initialize(AppSettings settings)
            throws AppConfigurationException {
        initializeSmtpSettings();
        // For now, nothing else to do. In the future may improve to include
        // message type, such as plain text, html etc.
    }

    @Override
    public String getComponentDescription() {
        return STL10019_MAIL_COMPONENT.getDescription();
    }

    @Override
    public int getInitializationWeight() {
        return 5;
    }

    public void initializeSmtpSettings() {
        this.mailProperties = getStoredMailProperties();
        if (!mailProperties.getEmailNotificationsEnabled()) {
            return;
        }

        if (isValidMailProperties(this.mailProperties)) {

            // Check if email notification feature is enabled
            createMailSender();

            // Settings are valide, we just dont need to instantiate mailSender
            isSmtpValid = true;
        } else {
            // SMTP settings are invalid. Send notice to UI.
            isSmtpValid = false;
            NoticeBean notice = createInvalidSMTPSettingsNotice();
            fireNotice(new NoticeBean[] { notice });
        }
    }

    public synchronized void updateMailProperties(MailProperties properties) {
        // Update the boolean variable about settings validity
        if (isValidMailProperties(properties) == true) {
            isSmtpValid = true;
        } else {
            // SMTP settings are invalid. Send notice to UI.
            NoticeBean notice = createInvalidSMTPSettingsNotice();
            isSmtpValid = false;
            fireNotice(new NoticeBean[] { notice });
        }
        mailProperties = properties;

        if (properties.getEmailNotificationsEnabled() && isSmtpValid) {
            // Make a new sender pointed at the new SMTP server.
            createMailSender();
        } else {
            // Remove reference to old mailSender.
            this.mailSender = null;
        }

        // Store the new SMTP settings in the database.
        storeMailProperties(properties);

    }

    public MailProperties getMailProperties() {
        return mailProperties;
    }

    public boolean isSmtpSettingsValid() {
        return isSmtpValid;
    }

    public void sendTestMail(MailProperties properties, String recipient,
            String messageSubject, String messageBody) {
        // Create a temporary mail sender with no send task.
        try {
            MailSender.sendTestMail(properties, recipient, messageSubject,
                    messageBody);
        } catch (MessagingException e) {
            NoticeBean bean = createInvalidSMTPSettingsNotice();
            fireNotice(new NoticeBean[] { bean });

        }
    }

    public void submitMessage(String subject, String body,
            List<String> recipients) {
        if (mailSender != null) {
            for (String recipient : recipients) {
                mailSender.submitMessage(recipient, subject, body);
            }
        } else {
            log.debug("MailManager: MailSender is null during submitMessage");
        }
    }

    @Override
    public void shutdown() {
        try {
            if (mailSender != null) {
                mailSender.shutdown();
            }
        } catch (Throwable e) {
            log.warn(StringUtils.getErrorMessage(e), e);
        }
    }

    protected void createMailSender() {
        if (mailSender == null) {
            try {
                mailSender = new MailSender(mailProperties);
            } catch (MessagingException e) {
                NoticeBean bean = createInvalidSMTPSettingsNotice();
                fireNotice(new NoticeBean[] { bean });
            }
        } else {
            try {
                mailSender.updateTransport(mailProperties);
            } catch (Exception e) {

            }
        }
        log.debug("MailManager: updateMailProperties called.");
    }

    protected MailProperties getStoredMailProperties() {
        AppInfo appInfo = databaseManager.getAppInfo();
        return unpackStoredMailProperties(appInfo);
    }

    protected void storeMailProperties(MailProperties mailProperties) {
        AppInfo appInfo = databaseManager.getAppInfo();
        Map<String, Properties> appProperties = appInfo.getPropertiesMap();
        Properties smtpProperties = packMailProperties(mailProperties);
        appProperties.put(AppInfo.PROPERTIES_SMTP_SETTINGS, smtpProperties);
        databaseManager.saveAppProperties(appProperties);
    }

    protected MailProperties unpackStoredMailProperties(AppInfo appInfo) {
        // Unpack SMTP Settings from database.
        MailProperties mailProperties = new MailProperties();
        if (appInfo != null) {
            Properties smtpProperties =
                    appInfo.getProperty(AppInfo.PROPERTIES_SMTP_SETTINGS);

            if (smtpProperties != null) {
                String fromAddress =
                        smtpProperties.getProperty(SMTP_FROM_ADDRESS);
                if (fromAddress != null) {
                    mailProperties.setFromAddr(fromAddress);
                }

                String portString = smtpProperties.getProperty(SMTP_PORT);
                if (portString != null) {
                    mailProperties.setSmtpPort(new Integer(portString));
                }

                String serverName =
                        smtpProperties.getProperty(SMTP_SERVER_NAME);
                if (serverName != null) {
                    mailProperties.setSmtpServer(serverName);
                }

                boolean enableEmailNotifications =
                        Boolean.parseBoolean(smtpProperties
                                .getProperty(SMTP_NOTIFICATIONS_ENABLED));
                mailProperties
                        .setEmailNotificationsEnabled(enableEmailNotifications);
            }
        }
        return mailProperties;
    }

    protected Properties packMailProperties(MailProperties properties) {
        Properties smtpProperties = new Properties();
        smtpProperties.setProperty(SMTP_FROM_ADDRESS, properties.getFromAddr());
        smtpProperties.setProperty(SMTP_PORT,
                new Integer(properties.getSmtpPort()).toString());
        smtpProperties
                .setProperty(SMTP_SERVER_NAME, properties.getSmtpServer());
        smtpProperties.setProperty(SMTP_NOTIFICATIONS_ENABLED,
                Boolean.toString(properties.getEmailNotificationsEnabled()));

        return smtpProperties;
    }

    protected boolean isValidMailProperties(MailProperties properties) {
        boolean isValid = false;
        // For now, we just check the essentials.
        if (properties != null) {
            if (!properties.getSmtpServer().isEmpty()
                    && properties.getSmtpPort() >= 0
                    && !properties.getFromAddr().isEmpty()) {
                isValid = true;
            }
        }
        return isValid;
    }

    protected NoticeBean createInvalidSMTPSettingsNotice() {
        NoticeBean bean = new NoticeBean(true);
        GenericNoticeAttrBean attr = new GenericNoticeAttrBean();
        attr.setGeneric(true);
        attr.setType(NoticeType.INFO.getId());
        attr.setTrapNumber(TrapType.SMTP_SETTINGS_INVALID.getId());
        bean.setAttributes(attr);
        bean.setData(STLMessages.STL70000_SMTP_UNABLE_TO_CONNECT
                .getDescription().getBytes());
        bean.setIssuerGID(new GIDGlobal());
        bean.setClassData(new byte[0]);
        return bean;
    }

    protected void fireNotice(NoticeBean[] notices) {
        log.info("Fire " + notices.length + " notices "
                + Arrays.toString(notices));
        if (emailEventListeners.isEmpty()) {
            synchronized (toSend) {
                for (NoticeBean notice : notices) {
                    toSend.add(notice);
                }
            }
        }
        for (IEmailEventListener<NoticeBean> listener : emailEventListeners) {
            try {
                listener.onNewEvent(notices);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addEmailEventListener(IEmailEventListener<NoticeBean> listener) {
        emailEventListeners.add(listener);
        synchronized (toSend) {
            if (!toSend.isEmpty()) {
                NoticeBean[] data = toSend.toArray(new NoticeBean[0]);
                toSend.clear();
                listener.onNewEvent(data);
            }
        }
    }

    public void removeEmailListener(IEmailEventListener<NoticeBean> listener) {
        emailEventListeners.remove(listener);
    }
}
