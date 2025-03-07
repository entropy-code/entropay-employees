package com.entropyteam.entropay.employees.envers;

import com.entropyteam.entropay.auth.AuthUtils;
import org.hibernate.envers.RevisionListener;

public class UserRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        var auditEnversInfo = (AuditEnversInfo) revisionEntity;
        auditEnversInfo.setUsername(AuthUtils.getUsername());
    }
}
