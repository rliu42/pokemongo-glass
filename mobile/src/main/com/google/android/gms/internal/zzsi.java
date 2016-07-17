package com.google.android.gms.internal;

import android.support.v4.media.TransportMediator;
import com.google.android.gms.location.places.Place;
import com.nianticproject.holoholo.sfida.SfidaMessage;
import com.nianticproject.holoholo.sfida.constants.BluetoothGattSupport;
import com.upsight.android.internal.persistence.subscription.Subscriptions;
import java.io.IOException;
import java.util.Arrays;
import spacemadness.com.lunarconsole.BuildConfig;
import spacemadness.com.lunarconsole.C1391R;

public interface zzsi {

    public static final class zza extends zzry<zza> {
        public String[] zzbiF;
        public String[] zzbiG;
        public int[] zzbiH;
        public long[] zzbiI;

        public zza() {
            zzFS();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zza)) {
                return false;
            }
            zza com_google_android_gms_internal_zzsi_zza = (zza) o;
            return (zzsc.equals(this.zzbiF, com_google_android_gms_internal_zzsi_zza.zzbiF) && zzsc.equals(this.zzbiG, com_google_android_gms_internal_zzsi_zza.zzbiG) && zzsc.equals(this.zzbiH, com_google_android_gms_internal_zzsi_zza.zzbiH) && zzsc.equals(this.zzbiI, com_google_android_gms_internal_zzsi_zza.zzbiI)) ? (this.zzbik == null || this.zzbik.isEmpty()) ? com_google_android_gms_internal_zzsi_zza.zzbik == null || com_google_android_gms_internal_zzsi_zza.zzbik.isEmpty() : this.zzbik.equals(com_google_android_gms_internal_zzsi_zza.zzbik) : false;
        }

        public int hashCode() {
            int hashCode = (((((((((getClass().getName().hashCode() + 527) * 31) + zzsc.hashCode(this.zzbiF)) * 31) + zzsc.hashCode(this.zzbiG)) * 31) + zzsc.hashCode(this.zzbiH)) * 31) + zzsc.hashCode(this.zzbiI)) * 31;
            int hashCode2 = (this.zzbik == null || this.zzbik.isEmpty()) ? 0 : this.zzbik.hashCode();
            return hashCode2 + hashCode;
        }

        protected int zzB() {
            int i;
            int i2;
            int i3;
            int i4 = 0;
            int zzB = super.zzB();
            if (this.zzbiF == null || this.zzbiF.length <= 0) {
                i = zzB;
            } else {
                i2 = 0;
                i3 = 0;
                for (String str : this.zzbiF) {
                    if (str != null) {
                        i3++;
                        i2 += zzrx.zzfA(str);
                    }
                }
                i = (zzB + i2) + (i3 * 1);
            }
            if (this.zzbiG != null && this.zzbiG.length > 0) {
                i3 = 0;
                zzB = 0;
                for (String str2 : this.zzbiG) {
                    if (str2 != null) {
                        zzB++;
                        i3 += zzrx.zzfA(str2);
                    }
                }
                i = (i + i3) + (zzB * 1);
            }
            if (this.zzbiH != null && this.zzbiH.length > 0) {
                i3 = 0;
                for (int zzB2 : this.zzbiH) {
                    i3 += zzrx.zzlJ(zzB2);
                }
                i = (i + i3) + (this.zzbiH.length * 1);
            }
            if (this.zzbiI == null || this.zzbiI.length <= 0) {
                return i;
            }
            i2 = 0;
            while (i4 < this.zzbiI.length) {
                i2 += zzrx.zzaa(this.zzbiI[i4]);
                i4++;
            }
            return (i + i2) + (this.zzbiI.length * 1);
        }

        public zza zzFS() {
            this.zzbiF = zzsh.zzbiC;
            this.zzbiG = zzsh.zzbiC;
            this.zzbiH = zzsh.zzbix;
            this.zzbiI = zzsh.zzbiy;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        public zza zzG(zzrw com_google_android_gms_internal_zzrw) throws IOException {
            while (true) {
                int zzFo = com_google_android_gms_internal_zzrw.zzFo();
                int zzc;
                Object obj;
                int zzlC;
                Object obj2;
                switch (zzFo) {
                    case C1391R.styleable.AdsAttrs_adSize /*0*/:
                        break;
                    case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                        zzc = zzsh.zzc(com_google_android_gms_internal_zzrw, 10);
                        zzFo = this.zzbiF == null ? 0 : this.zzbiF.length;
                        obj = new String[(zzc + zzFo)];
                        if (zzFo != 0) {
                            System.arraycopy(this.zzbiF, 0, obj, 0, zzFo);
                        }
                        while (zzFo < obj.length - 1) {
                            obj[zzFo] = com_google_android_gms_internal_zzrw.readString();
                            com_google_android_gms_internal_zzrw.zzFo();
                            zzFo++;
                        }
                        obj[zzFo] = com_google_android_gms_internal_zzrw.readString();
                        this.zzbiF = obj;
                        continue;
                    case Place.TYPE_CAR_RENTAL /*18*/:
                        zzc = zzsh.zzc(com_google_android_gms_internal_zzrw, 18);
                        zzFo = this.zzbiG == null ? 0 : this.zzbiG.length;
                        obj = new String[(zzc + zzFo)];
                        if (zzFo != 0) {
                            System.arraycopy(this.zzbiG, 0, obj, 0, zzFo);
                        }
                        while (zzFo < obj.length - 1) {
                            obj[zzFo] = com_google_android_gms_internal_zzrw.readString();
                            com_google_android_gms_internal_zzrw.zzFo();
                            zzFo++;
                        }
                        obj[zzFo] = com_google_android_gms_internal_zzrw.readString();
                        this.zzbiG = obj;
                        continue;
                    case Place.TYPE_CITY_HALL /*24*/:
                        zzc = zzsh.zzc(com_google_android_gms_internal_zzrw, 24);
                        zzFo = this.zzbiH == null ? 0 : this.zzbiH.length;
                        obj = new int[(zzc + zzFo)];
                        if (zzFo != 0) {
                            System.arraycopy(this.zzbiH, 0, obj, 0, zzFo);
                        }
                        while (zzFo < obj.length - 1) {
                            obj[zzFo] = com_google_android_gms_internal_zzrw.zzFr();
                            com_google_android_gms_internal_zzrw.zzFo();
                            zzFo++;
                        }
                        obj[zzFo] = com_google_android_gms_internal_zzrw.zzFr();
                        this.zzbiH = obj;
                        continue;
                    case Place.TYPE_CONVENIENCE_STORE /*26*/:
                        zzlC = com_google_android_gms_internal_zzrw.zzlC(com_google_android_gms_internal_zzrw.zzFv());
                        zzc = com_google_android_gms_internal_zzrw.getPosition();
                        zzFo = 0;
                        while (com_google_android_gms_internal_zzrw.zzFA() > 0) {
                            com_google_android_gms_internal_zzrw.zzFr();
                            zzFo++;
                        }
                        com_google_android_gms_internal_zzrw.zzlE(zzc);
                        zzc = this.zzbiH == null ? 0 : this.zzbiH.length;
                        obj2 = new int[(zzFo + zzc)];
                        if (zzc != 0) {
                            System.arraycopy(this.zzbiH, 0, obj2, 0, zzc);
                        }
                        while (zzc < obj2.length) {
                            obj2[zzc] = com_google_android_gms_internal_zzrw.zzFr();
                            zzc++;
                        }
                        this.zzbiH = obj2;
                        com_google_android_gms_internal_zzrw.zzlD(zzlC);
                        continue;
                    case Place.TYPE_ELECTRONICS_STORE /*32*/:
                        zzc = zzsh.zzc(com_google_android_gms_internal_zzrw, 32);
                        zzFo = this.zzbiI == null ? 0 : this.zzbiI.length;
                        obj = new long[(zzc + zzFo)];
                        if (zzFo != 0) {
                            System.arraycopy(this.zzbiI, 0, obj, 0, zzFo);
                        }
                        while (zzFo < obj.length - 1) {
                            obj[zzFo] = com_google_android_gms_internal_zzrw.zzFq();
                            com_google_android_gms_internal_zzrw.zzFo();
                            zzFo++;
                        }
                        obj[zzFo] = com_google_android_gms_internal_zzrw.zzFq();
                        this.zzbiI = obj;
                        continue;
                    case Place.TYPE_ESTABLISHMENT /*34*/:
                        zzlC = com_google_android_gms_internal_zzrw.zzlC(com_google_android_gms_internal_zzrw.zzFv());
                        zzc = com_google_android_gms_internal_zzrw.getPosition();
                        zzFo = 0;
                        while (com_google_android_gms_internal_zzrw.zzFA() > 0) {
                            com_google_android_gms_internal_zzrw.zzFq();
                            zzFo++;
                        }
                        com_google_android_gms_internal_zzrw.zzlE(zzc);
                        zzc = this.zzbiI == null ? 0 : this.zzbiI.length;
                        obj2 = new long[(zzFo + zzc)];
                        if (zzc != 0) {
                            System.arraycopy(this.zzbiI, 0, obj2, 0, zzc);
                        }
                        while (zzc < obj2.length) {
                            obj2[zzc] = com_google_android_gms_internal_zzrw.zzFq();
                            zzc++;
                        }
                        this.zzbiI = obj2;
                        com_google_android_gms_internal_zzrw.zzlD(zzlC);
                        continue;
                    default:
                        if (!zza(com_google_android_gms_internal_zzrw, zzFo)) {
                            break;
                        }
                        continue;
                }
                return this;
            }
        }

        public void zza(zzrx com_google_android_gms_internal_zzrx) throws IOException {
            int i = 0;
            if (this.zzbiF != null && this.zzbiF.length > 0) {
                for (String str : this.zzbiF) {
                    if (str != null) {
                        com_google_android_gms_internal_zzrx.zzb(1, str);
                    }
                }
            }
            if (this.zzbiG != null && this.zzbiG.length > 0) {
                for (String str2 : this.zzbiG) {
                    if (str2 != null) {
                        com_google_android_gms_internal_zzrx.zzb(2, str2);
                    }
                }
            }
            if (this.zzbiH != null && this.zzbiH.length > 0) {
                for (int zzy : this.zzbiH) {
                    com_google_android_gms_internal_zzrx.zzy(3, zzy);
                }
            }
            if (this.zzbiI != null && this.zzbiI.length > 0) {
                while (i < this.zzbiI.length) {
                    com_google_android_gms_internal_zzrx.zzb(4, this.zzbiI[i]);
                    i++;
                }
            }
            super.zza(com_google_android_gms_internal_zzrx);
        }

        public /* synthetic */ zzse zzb(zzrw com_google_android_gms_internal_zzrw) throws IOException {
            return zzG(com_google_android_gms_internal_zzrw);
        }
    }

    public static final class zzb extends zzry<zzb> {
        public String version;
        public int zzbiJ;
        public String zzbiK;

        public zzb() {
            zzFT();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzb)) {
                return false;
            }
            zzb com_google_android_gms_internal_zzsi_zzb = (zzb) o;
            if (this.zzbiJ != com_google_android_gms_internal_zzsi_zzb.zzbiJ) {
                return false;
            }
            if (this.zzbiK == null) {
                if (com_google_android_gms_internal_zzsi_zzb.zzbiK != null) {
                    return false;
                }
            } else if (!this.zzbiK.equals(com_google_android_gms_internal_zzsi_zzb.zzbiK)) {
                return false;
            }
            if (this.version == null) {
                if (com_google_android_gms_internal_zzsi_zzb.version != null) {
                    return false;
                }
            } else if (!this.version.equals(com_google_android_gms_internal_zzsi_zzb.version)) {
                return false;
            }
            return (this.zzbik == null || this.zzbik.isEmpty()) ? com_google_android_gms_internal_zzsi_zzb.zzbik == null || com_google_android_gms_internal_zzsi_zzb.zzbik.isEmpty() : this.zzbik.equals(com_google_android_gms_internal_zzsi_zzb.zzbik);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((this.version == null ? 0 : this.version.hashCode()) + (((this.zzbiK == null ? 0 : this.zzbiK.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + this.zzbiJ) * 31)) * 31)) * 31;
            if (!(this.zzbik == null || this.zzbik.isEmpty())) {
                i = this.zzbik.hashCode();
            }
            return hashCode + i;
        }

        protected int zzB() {
            int zzB = super.zzB();
            if (this.zzbiJ != 0) {
                zzB += zzrx.zzA(1, this.zzbiJ);
            }
            if (!this.zzbiK.equals(BuildConfig.FLAVOR)) {
                zzB += zzrx.zzn(2, this.zzbiK);
            }
            return !this.version.equals(BuildConfig.FLAVOR) ? zzB + zzrx.zzn(3, this.version) : zzB;
        }

        public zzb zzFT() {
            this.zzbiJ = 0;
            this.zzbiK = BuildConfig.FLAVOR;
            this.version = BuildConfig.FLAVOR;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        public zzb zzH(zzrw com_google_android_gms_internal_zzrw) throws IOException {
            while (true) {
                int zzFo = com_google_android_gms_internal_zzrw.zzFo();
                switch (zzFo) {
                    case C1391R.styleable.AdsAttrs_adSize /*0*/:
                        break;
                    case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                        zzFo = com_google_android_gms_internal_zzrw.zzFr();
                        switch (zzFo) {
                            case C1391R.styleable.AdsAttrs_adSize /*0*/:
                            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                            case SfidaMessage.ACTIVITY_BYTE_LENGTH /*3*/:
                            case Place.TYPE_AQUARIUM /*4*/:
                            case Place.TYPE_ART_GALLERY /*5*/:
                            case Place.TYPE_ATM /*6*/:
                            case Place.TYPE_BAKERY /*7*/:
                            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                            case Place.TYPE_BAR /*9*/:
                            case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                            case Place.TYPE_BICYCLE_STORE /*11*/:
                            case Place.TYPE_BOOK_STORE /*12*/:
                            case Place.TYPE_BOWLING_ALLEY /*13*/:
                            case Place.TYPE_BUS_STATION /*14*/:
                            case Place.TYPE_CAFE /*15*/:
                            case Place.TYPE_CAMPGROUND /*16*/:
                            case Place.TYPE_CAR_DEALER /*17*/:
                            case Place.TYPE_CAR_RENTAL /*18*/:
                            case Place.TYPE_CAR_REPAIR /*19*/:
                            case Place.TYPE_CAR_WASH /*20*/:
                            case Place.TYPE_CASINO /*21*/:
                            case Place.TYPE_CEMETERY /*22*/:
                            case Place.TYPE_CHURCH /*23*/:
                            case Place.TYPE_CITY_HALL /*24*/:
                            case Place.TYPE_CLOTHING_STORE /*25*/:
                            case Place.TYPE_CONVENIENCE_STORE /*26*/:
                                this.zzbiJ = zzFo;
                                break;
                            default:
                                continue;
                        }
                    case Place.TYPE_CAR_RENTAL /*18*/:
                        this.zzbiK = com_google_android_gms_internal_zzrw.readString();
                        continue;
                    case Place.TYPE_CONVENIENCE_STORE /*26*/:
                        this.version = com_google_android_gms_internal_zzrw.readString();
                        continue;
                    default:
                        if (!zza(com_google_android_gms_internal_zzrw, zzFo)) {
                            break;
                        }
                        continue;
                }
                return this;
            }
        }

        public void zza(zzrx com_google_android_gms_internal_zzrx) throws IOException {
            if (this.zzbiJ != 0) {
                com_google_android_gms_internal_zzrx.zzy(1, this.zzbiJ);
            }
            if (!this.zzbiK.equals(BuildConfig.FLAVOR)) {
                com_google_android_gms_internal_zzrx.zzb(2, this.zzbiK);
            }
            if (!this.version.equals(BuildConfig.FLAVOR)) {
                com_google_android_gms_internal_zzrx.zzb(3, this.version);
            }
            super.zza(com_google_android_gms_internal_zzrx);
        }

        public /* synthetic */ zzse zzb(zzrw com_google_android_gms_internal_zzrw) throws IOException {
            return zzH(com_google_android_gms_internal_zzrw);
        }
    }

    public static final class zzc extends zzry<zzc> {
        public byte[] zzbiL;
        public byte[][] zzbiM;
        public boolean zzbiN;

        public zzc() {
            zzFU();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzc)) {
                return false;
            }
            zzc com_google_android_gms_internal_zzsi_zzc = (zzc) o;
            return (Arrays.equals(this.zzbiL, com_google_android_gms_internal_zzsi_zzc.zzbiL) && zzsc.zza(this.zzbiM, com_google_android_gms_internal_zzsi_zzc.zzbiM) && this.zzbiN == com_google_android_gms_internal_zzsi_zzc.zzbiN) ? (this.zzbik == null || this.zzbik.isEmpty()) ? com_google_android_gms_internal_zzsi_zzc.zzbik == null || com_google_android_gms_internal_zzsi_zzc.zzbik.isEmpty() : this.zzbik.equals(com_google_android_gms_internal_zzsi_zzc.zzbik) : false;
        }

        public int hashCode() {
            int hashCode = ((this.zzbiN ? 1231 : 1237) + ((((((getClass().getName().hashCode() + 527) * 31) + Arrays.hashCode(this.zzbiL)) * 31) + zzsc.zza(this.zzbiM)) * 31)) * 31;
            int hashCode2 = (this.zzbik == null || this.zzbik.isEmpty()) ? 0 : this.zzbik.hashCode();
            return hashCode2 + hashCode;
        }

        protected int zzB() {
            int i = 0;
            int zzB = super.zzB();
            if (!Arrays.equals(this.zzbiL, zzsh.zzbiE)) {
                zzB += zzrx.zzb(1, this.zzbiL);
            }
            if (this.zzbiM != null && this.zzbiM.length > 0) {
                int i2 = 0;
                int i3 = 0;
                while (i < this.zzbiM.length) {
                    byte[] bArr = this.zzbiM[i];
                    if (bArr != null) {
                        i3++;
                        i2 += zzrx.zzE(bArr);
                    }
                    i++;
                }
                zzB = (zzB + i2) + (i3 * 1);
            }
            return this.zzbiN ? zzB + zzrx.zzc(3, this.zzbiN) : zzB;
        }

        public zzc zzFU() {
            this.zzbiL = zzsh.zzbiE;
            this.zzbiM = zzsh.zzbiD;
            this.zzbiN = false;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        public zzc zzI(zzrw com_google_android_gms_internal_zzrw) throws IOException {
            while (true) {
                int zzFo = com_google_android_gms_internal_zzrw.zzFo();
                switch (zzFo) {
                    case C1391R.styleable.AdsAttrs_adSize /*0*/:
                        break;
                    case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                        this.zzbiL = com_google_android_gms_internal_zzrw.readBytes();
                        continue;
                    case Place.TYPE_CAR_RENTAL /*18*/:
                        int zzc = zzsh.zzc(com_google_android_gms_internal_zzrw, 18);
                        zzFo = this.zzbiM == null ? 0 : this.zzbiM.length;
                        Object obj = new byte[(zzc + zzFo)][];
                        if (zzFo != 0) {
                            System.arraycopy(this.zzbiM, 0, obj, 0, zzFo);
                        }
                        while (zzFo < obj.length - 1) {
                            obj[zzFo] = com_google_android_gms_internal_zzrw.readBytes();
                            com_google_android_gms_internal_zzrw.zzFo();
                            zzFo++;
                        }
                        obj[zzFo] = com_google_android_gms_internal_zzrw.readBytes();
                        this.zzbiM = obj;
                        continue;
                    case Place.TYPE_CITY_HALL /*24*/:
                        this.zzbiN = com_google_android_gms_internal_zzrw.zzFs();
                        continue;
                    default:
                        if (!zza(com_google_android_gms_internal_zzrw, zzFo)) {
                            break;
                        }
                        continue;
                }
                return this;
            }
        }

        public void zza(zzrx com_google_android_gms_internal_zzrx) throws IOException {
            if (!Arrays.equals(this.zzbiL, zzsh.zzbiE)) {
                com_google_android_gms_internal_zzrx.zza(1, this.zzbiL);
            }
            if (this.zzbiM != null && this.zzbiM.length > 0) {
                for (byte[] bArr : this.zzbiM) {
                    if (bArr != null) {
                        com_google_android_gms_internal_zzrx.zza(2, bArr);
                    }
                }
            }
            if (this.zzbiN) {
                com_google_android_gms_internal_zzrx.zzb(3, this.zzbiN);
            }
            super.zza(com_google_android_gms_internal_zzrx);
        }

        public /* synthetic */ zzse zzb(zzrw com_google_android_gms_internal_zzrw) throws IOException {
            return zzI(com_google_android_gms_internal_zzrw);
        }
    }

    public static final class zzd extends zzry<zzd> {
        public String tag;
        public long zzbiO;
        public long zzbiP;
        public int zzbiQ;
        public int zzbiR;
        public boolean zzbiS;
        public zze[] zzbiT;
        public zzb zzbiU;
        public byte[] zzbiV;
        public byte[] zzbiW;
        public byte[] zzbiX;
        public zza zzbiY;
        public String zzbiZ;
        public long zzbja;
        public zzc zzbjb;
        public byte[] zzbjc;
        public int zzbjd;
        public int[] zzbje;

        public zzd() {
            zzFV();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzd)) {
                return false;
            }
            zzd com_google_android_gms_internal_zzsi_zzd = (zzd) o;
            if (this.zzbiO != com_google_android_gms_internal_zzsi_zzd.zzbiO || this.zzbiP != com_google_android_gms_internal_zzsi_zzd.zzbiP) {
                return false;
            }
            if (this.tag == null) {
                if (com_google_android_gms_internal_zzsi_zzd.tag != null) {
                    return false;
                }
            } else if (!this.tag.equals(com_google_android_gms_internal_zzsi_zzd.tag)) {
                return false;
            }
            if (this.zzbiQ != com_google_android_gms_internal_zzsi_zzd.zzbiQ || this.zzbiR != com_google_android_gms_internal_zzsi_zzd.zzbiR || this.zzbiS != com_google_android_gms_internal_zzsi_zzd.zzbiS || !zzsc.equals(this.zzbiT, com_google_android_gms_internal_zzsi_zzd.zzbiT)) {
                return false;
            }
            if (this.zzbiU == null) {
                if (com_google_android_gms_internal_zzsi_zzd.zzbiU != null) {
                    return false;
                }
            } else if (!this.zzbiU.equals(com_google_android_gms_internal_zzsi_zzd.zzbiU)) {
                return false;
            }
            if (!Arrays.equals(this.zzbiV, com_google_android_gms_internal_zzsi_zzd.zzbiV) || !Arrays.equals(this.zzbiW, com_google_android_gms_internal_zzsi_zzd.zzbiW) || !Arrays.equals(this.zzbiX, com_google_android_gms_internal_zzsi_zzd.zzbiX)) {
                return false;
            }
            if (this.zzbiY == null) {
                if (com_google_android_gms_internal_zzsi_zzd.zzbiY != null) {
                    return false;
                }
            } else if (!this.zzbiY.equals(com_google_android_gms_internal_zzsi_zzd.zzbiY)) {
                return false;
            }
            if (this.zzbiZ == null) {
                if (com_google_android_gms_internal_zzsi_zzd.zzbiZ != null) {
                    return false;
                }
            } else if (!this.zzbiZ.equals(com_google_android_gms_internal_zzsi_zzd.zzbiZ)) {
                return false;
            }
            if (this.zzbja != com_google_android_gms_internal_zzsi_zzd.zzbja) {
                return false;
            }
            if (this.zzbjb == null) {
                if (com_google_android_gms_internal_zzsi_zzd.zzbjb != null) {
                    return false;
                }
            } else if (!this.zzbjb.equals(com_google_android_gms_internal_zzsi_zzd.zzbjb)) {
                return false;
            }
            return (Arrays.equals(this.zzbjc, com_google_android_gms_internal_zzsi_zzd.zzbjc) && this.zzbjd == com_google_android_gms_internal_zzsi_zzd.zzbjd && zzsc.equals(this.zzbje, com_google_android_gms_internal_zzsi_zzd.zzbje)) ? (this.zzbik == null || this.zzbik.isEmpty()) ? com_google_android_gms_internal_zzsi_zzd.zzbik == null || com_google_android_gms_internal_zzsi_zzd.zzbik.isEmpty() : this.zzbik.equals(com_google_android_gms_internal_zzsi_zzd.zzbik) : false;
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((((((((this.zzbjb == null ? 0 : this.zzbjb.hashCode()) + (((((this.zzbiZ == null ? 0 : this.zzbiZ.hashCode()) + (((this.zzbiY == null ? 0 : this.zzbiY.hashCode()) + (((((((((this.zzbiU == null ? 0 : this.zzbiU.hashCode()) + (((((this.zzbiS ? 1231 : 1237) + (((((((this.tag == null ? 0 : this.tag.hashCode()) + ((((((getClass().getName().hashCode() + 527) * 31) + ((int) (this.zzbiO ^ (this.zzbiO >>> 32)))) * 31) + ((int) (this.zzbiP ^ (this.zzbiP >>> 32)))) * 31)) * 31) + this.zzbiQ) * 31) + this.zzbiR) * 31)) * 31) + zzsc.hashCode(this.zzbiT)) * 31)) * 31) + Arrays.hashCode(this.zzbiV)) * 31) + Arrays.hashCode(this.zzbiW)) * 31) + Arrays.hashCode(this.zzbiX)) * 31)) * 31)) * 31) + ((int) (this.zzbja ^ (this.zzbja >>> 32)))) * 31)) * 31) + Arrays.hashCode(this.zzbjc)) * 31) + this.zzbjd) * 31) + zzsc.hashCode(this.zzbje)) * 31;
            if (!(this.zzbik == null || this.zzbik.isEmpty())) {
                i = this.zzbik.hashCode();
            }
            return hashCode + i;
        }

        protected int zzB() {
            int i;
            int i2 = 0;
            int zzB = super.zzB();
            if (this.zzbiO != 0) {
                zzB += zzrx.zzd(1, this.zzbiO);
            }
            if (!this.tag.equals(BuildConfig.FLAVOR)) {
                zzB += zzrx.zzn(2, this.tag);
            }
            if (this.zzbiT != null && this.zzbiT.length > 0) {
                i = zzB;
                for (zzse com_google_android_gms_internal_zzse : this.zzbiT) {
                    if (com_google_android_gms_internal_zzse != null) {
                        i += zzrx.zzc(3, com_google_android_gms_internal_zzse);
                    }
                }
                zzB = i;
            }
            if (!Arrays.equals(this.zzbiV, zzsh.zzbiE)) {
                zzB += zzrx.zzb(6, this.zzbiV);
            }
            if (this.zzbiY != null) {
                zzB += zzrx.zzc(7, this.zzbiY);
            }
            if (!Arrays.equals(this.zzbiW, zzsh.zzbiE)) {
                zzB += zzrx.zzb(8, this.zzbiW);
            }
            if (this.zzbiU != null) {
                zzB += zzrx.zzc(9, this.zzbiU);
            }
            if (this.zzbiS) {
                zzB += zzrx.zzc(10, this.zzbiS);
            }
            if (this.zzbiQ != 0) {
                zzB += zzrx.zzA(11, this.zzbiQ);
            }
            if (this.zzbiR != 0) {
                zzB += zzrx.zzA(12, this.zzbiR);
            }
            if (!Arrays.equals(this.zzbiX, zzsh.zzbiE)) {
                zzB += zzrx.zzb(13, this.zzbiX);
            }
            if (!this.zzbiZ.equals(BuildConfig.FLAVOR)) {
                zzB += zzrx.zzn(14, this.zzbiZ);
            }
            if (this.zzbja != 180000) {
                zzB += zzrx.zze(15, this.zzbja);
            }
            if (this.zzbjb != null) {
                zzB += zzrx.zzc(16, this.zzbjb);
            }
            if (this.zzbiP != 0) {
                zzB += zzrx.zzd(17, this.zzbiP);
            }
            if (!Arrays.equals(this.zzbjc, zzsh.zzbiE)) {
                zzB += zzrx.zzb(18, this.zzbjc);
            }
            if (this.zzbjd != 0) {
                zzB += zzrx.zzA(19, this.zzbjd);
            }
            if (this.zzbje == null || this.zzbje.length <= 0) {
                return zzB;
            }
            i = 0;
            while (i2 < this.zzbje.length) {
                i += zzrx.zzlJ(this.zzbje[i2]);
                i2++;
            }
            return (zzB + i) + (this.zzbje.length * 2);
        }

        public zzd zzFV() {
            this.zzbiO = 0;
            this.zzbiP = 0;
            this.tag = BuildConfig.FLAVOR;
            this.zzbiQ = 0;
            this.zzbiR = 0;
            this.zzbiS = false;
            this.zzbiT = zze.zzFW();
            this.zzbiU = null;
            this.zzbiV = zzsh.zzbiE;
            this.zzbiW = zzsh.zzbiE;
            this.zzbiX = zzsh.zzbiE;
            this.zzbiY = null;
            this.zzbiZ = BuildConfig.FLAVOR;
            this.zzbja = 180000;
            this.zzbjb = null;
            this.zzbjc = zzsh.zzbiE;
            this.zzbjd = 0;
            this.zzbje = zzsh.zzbix;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        public zzd zzJ(zzrw com_google_android_gms_internal_zzrw) throws IOException {
            while (true) {
                int zzFo = com_google_android_gms_internal_zzrw.zzFo();
                int zzc;
                Object obj;
                switch (zzFo) {
                    case C1391R.styleable.AdsAttrs_adSize /*0*/:
                        break;
                    case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                        this.zzbiO = com_google_android_gms_internal_zzrw.zzFq();
                        continue;
                    case Place.TYPE_CAR_RENTAL /*18*/:
                        this.tag = com_google_android_gms_internal_zzrw.readString();
                        continue;
                    case Place.TYPE_CONVENIENCE_STORE /*26*/:
                        zzc = zzsh.zzc(com_google_android_gms_internal_zzrw, 26);
                        zzFo = this.zzbiT == null ? 0 : this.zzbiT.length;
                        obj = new zze[(zzc + zzFo)];
                        if (zzFo != 0) {
                            System.arraycopy(this.zzbiT, 0, obj, 0, zzFo);
                        }
                        while (zzFo < obj.length - 1) {
                            obj[zzFo] = new zze();
                            com_google_android_gms_internal_zzrw.zza(obj[zzFo]);
                            com_google_android_gms_internal_zzrw.zzFo();
                            zzFo++;
                        }
                        obj[zzFo] = new zze();
                        com_google_android_gms_internal_zzrw.zza(obj[zzFo]);
                        this.zzbiT = obj;
                        continue;
                    case Place.TYPE_HOSPITAL /*50*/:
                        this.zzbiV = com_google_android_gms_internal_zzrw.readBytes();
                        continue;
                    case Place.TYPE_LOCKSMITH /*58*/:
                        if (this.zzbiY == null) {
                            this.zzbiY = new zza();
                        }
                        com_google_android_gms_internal_zzrw.zza(this.zzbiY);
                        continue;
                    case Place.TYPE_MUSEUM /*66*/:
                        this.zzbiW = com_google_android_gms_internal_zzrw.readBytes();
                        continue;
                    case Place.TYPE_PLACE_OF_WORSHIP /*74*/:
                        if (this.zzbiU == null) {
                            this.zzbiU = new zzb();
                        }
                        com_google_android_gms_internal_zzrw.zza(this.zzbiU);
                        continue;
                    case Place.TYPE_ROOFING_CONTRACTOR /*80*/:
                        this.zzbiS = com_google_android_gms_internal_zzrw.zzFs();
                        continue;
                    case Place.TYPE_STORE /*88*/:
                        this.zzbiQ = com_google_android_gms_internal_zzrw.zzFr();
                        continue;
                    case Place.TYPE_ZOO /*96*/:
                        this.zzbiR = com_google_android_gms_internal_zzrw.zzFr();
                        continue;
                    case 106:
                        this.zzbiX = com_google_android_gms_internal_zzrw.readBytes();
                        continue;
                    case 114:
                        this.zzbiZ = com_google_android_gms_internal_zzrw.readString();
                        continue;
                    case 120:
                        this.zzbja = com_google_android_gms_internal_zzrw.zzFu();
                        continue;
                    case TransportMediator.KEYCODE_MEDIA_RECORD /*130*/:
                        if (this.zzbjb == null) {
                            this.zzbjb = new zzc();
                        }
                        com_google_android_gms_internal_zzrw.zza(this.zzbjb);
                        continue;
                    case 136:
                        this.zzbiP = com_google_android_gms_internal_zzrw.zzFq();
                        continue;
                    case 146:
                        this.zzbjc = com_google_android_gms_internal_zzrw.readBytes();
                        continue;
                    case 152:
                        zzFo = com_google_android_gms_internal_zzrw.zzFr();
                        switch (zzFo) {
                            case C1391R.styleable.AdsAttrs_adSize /*0*/:
                            case C1391R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                            case C1391R.styleable.LoadingImageView_circleCrop /*2*/:
                                this.zzbjd = zzFo;
                                break;
                            default:
                                continue;
                        }
                    case 160:
                        zzc = zzsh.zzc(com_google_android_gms_internal_zzrw, 160);
                        zzFo = this.zzbje == null ? 0 : this.zzbje.length;
                        obj = new int[(zzc + zzFo)];
                        if (zzFo != 0) {
                            System.arraycopy(this.zzbje, 0, obj, 0, zzFo);
                        }
                        while (zzFo < obj.length - 1) {
                            obj[zzFo] = com_google_android_gms_internal_zzrw.zzFr();
                            com_google_android_gms_internal_zzrw.zzFo();
                            zzFo++;
                        }
                        obj[zzFo] = com_google_android_gms_internal_zzrw.zzFr();
                        this.zzbje = obj;
                        continue;
                    case 162:
                        int zzlC = com_google_android_gms_internal_zzrw.zzlC(com_google_android_gms_internal_zzrw.zzFv());
                        zzc = com_google_android_gms_internal_zzrw.getPosition();
                        zzFo = 0;
                        while (com_google_android_gms_internal_zzrw.zzFA() > 0) {
                            com_google_android_gms_internal_zzrw.zzFr();
                            zzFo++;
                        }
                        com_google_android_gms_internal_zzrw.zzlE(zzc);
                        zzc = this.zzbje == null ? 0 : this.zzbje.length;
                        Object obj2 = new int[(zzFo + zzc)];
                        if (zzc != 0) {
                            System.arraycopy(this.zzbje, 0, obj2, 0, zzc);
                        }
                        while (zzc < obj2.length) {
                            obj2[zzc] = com_google_android_gms_internal_zzrw.zzFr();
                            zzc++;
                        }
                        this.zzbje = obj2;
                        com_google_android_gms_internal_zzrw.zzlD(zzlC);
                        continue;
                    default:
                        if (!zza(com_google_android_gms_internal_zzrw, zzFo)) {
                            break;
                        }
                        continue;
                }
                return this;
            }
        }

        public void zza(zzrx com_google_android_gms_internal_zzrx) throws IOException {
            int i = 0;
            if (this.zzbiO != 0) {
                com_google_android_gms_internal_zzrx.zzb(1, this.zzbiO);
            }
            if (!this.tag.equals(BuildConfig.FLAVOR)) {
                com_google_android_gms_internal_zzrx.zzb(2, this.tag);
            }
            if (this.zzbiT != null && this.zzbiT.length > 0) {
                for (zzse com_google_android_gms_internal_zzse : this.zzbiT) {
                    if (com_google_android_gms_internal_zzse != null) {
                        com_google_android_gms_internal_zzrx.zza(3, com_google_android_gms_internal_zzse);
                    }
                }
            }
            if (!Arrays.equals(this.zzbiV, zzsh.zzbiE)) {
                com_google_android_gms_internal_zzrx.zza(6, this.zzbiV);
            }
            if (this.zzbiY != null) {
                com_google_android_gms_internal_zzrx.zza(7, this.zzbiY);
            }
            if (!Arrays.equals(this.zzbiW, zzsh.zzbiE)) {
                com_google_android_gms_internal_zzrx.zza(8, this.zzbiW);
            }
            if (this.zzbiU != null) {
                com_google_android_gms_internal_zzrx.zza(9, this.zzbiU);
            }
            if (this.zzbiS) {
                com_google_android_gms_internal_zzrx.zzb(10, this.zzbiS);
            }
            if (this.zzbiQ != 0) {
                com_google_android_gms_internal_zzrx.zzy(11, this.zzbiQ);
            }
            if (this.zzbiR != 0) {
                com_google_android_gms_internal_zzrx.zzy(12, this.zzbiR);
            }
            if (!Arrays.equals(this.zzbiX, zzsh.zzbiE)) {
                com_google_android_gms_internal_zzrx.zza(13, this.zzbiX);
            }
            if (!this.zzbiZ.equals(BuildConfig.FLAVOR)) {
                com_google_android_gms_internal_zzrx.zzb(14, this.zzbiZ);
            }
            if (this.zzbja != 180000) {
                com_google_android_gms_internal_zzrx.zzc(15, this.zzbja);
            }
            if (this.zzbjb != null) {
                com_google_android_gms_internal_zzrx.zza(16, this.zzbjb);
            }
            if (this.zzbiP != 0) {
                com_google_android_gms_internal_zzrx.zzb(17, this.zzbiP);
            }
            if (!Arrays.equals(this.zzbjc, zzsh.zzbiE)) {
                com_google_android_gms_internal_zzrx.zza(18, this.zzbjc);
            }
            if (this.zzbjd != 0) {
                com_google_android_gms_internal_zzrx.zzy(19, this.zzbjd);
            }
            if (this.zzbje != null && this.zzbje.length > 0) {
                while (i < this.zzbje.length) {
                    com_google_android_gms_internal_zzrx.zzy(20, this.zzbje[i]);
                    i++;
                }
            }
            super.zza(com_google_android_gms_internal_zzrx);
        }

        public /* synthetic */ zzse zzb(zzrw com_google_android_gms_internal_zzrw) throws IOException {
            return zzJ(com_google_android_gms_internal_zzrw);
        }
    }

    public static final class zze extends zzry<zze> {
        private static volatile zze[] zzbjf;
        public String key;
        public String value;

        public zze() {
            zzFX();
        }

        public static zze[] zzFW() {
            if (zzbjf == null) {
                synchronized (zzsc.zzbiu) {
                    if (zzbjf == null) {
                        zzbjf = new zze[0];
                    }
                }
            }
            return zzbjf;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zze)) {
                return false;
            }
            zze com_google_android_gms_internal_zzsi_zze = (zze) o;
            if (this.key == null) {
                if (com_google_android_gms_internal_zzsi_zze.key != null) {
                    return false;
                }
            } else if (!this.key.equals(com_google_android_gms_internal_zzsi_zze.key)) {
                return false;
            }
            if (this.value == null) {
                if (com_google_android_gms_internal_zzsi_zze.value != null) {
                    return false;
                }
            } else if (!this.value.equals(com_google_android_gms_internal_zzsi_zze.value)) {
                return false;
            }
            return (this.zzbik == null || this.zzbik.isEmpty()) ? com_google_android_gms_internal_zzsi_zze.zzbik == null || com_google_android_gms_internal_zzsi_zze.zzbik.isEmpty() : this.zzbik.equals(com_google_android_gms_internal_zzsi_zze.zzbik);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((this.value == null ? 0 : this.value.hashCode()) + (((this.key == null ? 0 : this.key.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31;
            if (!(this.zzbik == null || this.zzbik.isEmpty())) {
                i = this.zzbik.hashCode();
            }
            return hashCode + i;
        }

        protected int zzB() {
            int zzB = super.zzB();
            if (!this.key.equals(BuildConfig.FLAVOR)) {
                zzB += zzrx.zzn(1, this.key);
            }
            return !this.value.equals(BuildConfig.FLAVOR) ? zzB + zzrx.zzn(2, this.value) : zzB;
        }

        public zze zzFX() {
            this.key = BuildConfig.FLAVOR;
            this.value = BuildConfig.FLAVOR;
            this.zzbik = null;
            this.zzbiv = -1;
            return this;
        }

        public zze zzK(zzrw com_google_android_gms_internal_zzrw) throws IOException {
            while (true) {
                int zzFo = com_google_android_gms_internal_zzrw.zzFo();
                switch (zzFo) {
                    case C1391R.styleable.AdsAttrs_adSize /*0*/:
                        break;
                    case Subscriptions.MAX_QUEUE_LENGTH /*10*/:
                        this.key = com_google_android_gms_internal_zzrw.readString();
                        continue;
                    case Place.TYPE_CAR_RENTAL /*18*/:
                        this.value = com_google_android_gms_internal_zzrw.readString();
                        continue;
                    default:
                        if (!zza(com_google_android_gms_internal_zzrw, zzFo)) {
                            break;
                        }
                        continue;
                }
                return this;
            }
        }

        public void zza(zzrx com_google_android_gms_internal_zzrx) throws IOException {
            if (!this.key.equals(BuildConfig.FLAVOR)) {
                com_google_android_gms_internal_zzrx.zzb(1, this.key);
            }
            if (!this.value.equals(BuildConfig.FLAVOR)) {
                com_google_android_gms_internal_zzrx.zzb(2, this.value);
            }
            super.zza(com_google_android_gms_internal_zzrx);
        }

        public /* synthetic */ zzse zzb(zzrw com_google_android_gms_internal_zzrw) throws IOException {
            return zzK(com_google_android_gms_internal_zzrw);
        }
    }
}
