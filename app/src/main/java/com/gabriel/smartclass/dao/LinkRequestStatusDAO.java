package com.gabriel.smartclass.dao;

import com.gabriel.smartclass.model.LinkRequestStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LinkRequestStatusDAO {
    public static DocumentReference APPROVED_REFERENCE = FirebaseFirestore.getInstance().collection(LinkRequestStatus.class.getSimpleName()).document("BRt3BuuelCefADoY3gCB");
    public static DocumentReference REJECTED_REFERENCE = FirebaseFirestore.getInstance().collection(LinkRequestStatus.class.getSimpleName()).document("msW4p5Ik8nMJgOyraQRj");
    public static DocumentReference PENDING_REFERENCE = FirebaseFirestore.getInstance().collection(LinkRequestStatus.class.getSimpleName()).document("GepOdUXTKZoMEt35eJVm");
}
