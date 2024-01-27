package com.dev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dev.body.Annoncesave;
import com.dev.exception.ExceptionCar;
import com.dev.models.AdminsMi;
import com.dev.models.AnnonceMi;
import com.dev.models.AnnoncephotoMi;
import com.dev.models.AnnoncerefusMi;
import com.dev.models.AnnoncevalidationMi;
import com.dev.models.CreditersoldesiteMi;
import com.dev.models.DebitersoldeuserMi;
import com.dev.models.ImageService;
import com.dev.models.MotifMi;
import com.dev.models.SoldesiteMi;
import com.dev.models.SoldeuserMi;
import com.dev.models.VenduMi;
import com.dev.repository.AnnonceMiRep;
import com.dev.repository.AnnoncephotoMiRep;
import com.dev.repository.CreditersoldesiteMiRep;
import com.dev.repository.DebitersoldeuserMiRep;
import com.dev.repository.SoldesiteMiRep;
import com.dev.repository.SoldeuserMiRep;
import com.dev.repository.VenduMiRep;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AnnonceMiSer {

    @Autowired
    private AnnonceMiRep annonceRepository;
    @Autowired
    private AnnoncephotoMiRep annoncephotoRepository;
    @Autowired
    private AnnoncerefusMiSer annoncerefusMiSer;
    @Autowired
    private AnnoncevalidationMiSer annoncevalidationMiSer;
    @Autowired
    private AdminsMiSer adminsMiSer;
    @Autowired
    private SoldeuserMiSer soldeuserMiSer;
    @Autowired
    private SoldeuserMiRep soldeuserMiRep;
    @Autowired
    private RegletauxMiSer regletauxMiSer;
    @Autowired
    private DebitersoldeuserMiRep debitersoldeuserMiRep;
    @Autowired
    private MotifMiSer motifMiSer;
    @Autowired
    private CreditersoldesiteMiRep creditersoldesiteMiRep;
    @Autowired
    private SoldesiteMiSer soldesiteMiSer;
    @Autowired
    private SoldesiteMiRep soldesiteMiRep;
    @Autowired
    private VenduMiRep venduMiRep;
    
    public AnnonceMi save(AnnonceMi Annonce) {
        return annonceRepository.save(Annonce);
    }
    @Transactional(rollbackFor = { Exception.class, ExceptionCar.class })
    public void valider(int idannonce,int idadmin)throws Exception{

        Optional<AdminsMi> opAdm=adminsMiSer.getById(idadmin);
        if(opAdm==null){ throw new ExceptionCar("vous n'avez pas l'autorisation pour cette operation"); }
        AdminsMi adminsMi=opAdm.get();
        if(adminsMi==null){ throw new ExceptionCar("vous n'avez pas l'autorisation pour cette operation"); }

        Optional<AnnonceMi> opAn=getById(idannonce);
        if(opAn==null){ throw new ExceptionCar("annonce id:"+idannonce+" invalide"); }
        AnnonceMi annonceMi=opAn.get();
        if(annonceMi==null){ throw new ExceptionCar("annonce id:"+idannonce+" invalide"); }
        if(annonceMi.estEncours()==false){ throw new ExceptionCar("operation failed,annonce n'est plus encours de demande"); }
        else{ annonceMi.setEtatToValide(); }
        AnnoncevalidationMi annoncevalidationMi=new AnnoncevalidationMi(0, Date.valueOf(LocalDate.now()), idadmin, idannonce);
        annoncevalidationMiSer.save(annoncevalidationMi);
        save(annonceMi);//UPDATE ETAT
    }
    @Transactional(rollbackFor = { Exception.class, ExceptionCar.class })
    public void refuser(int idannonce,int idadmin)throws Exception{
        Optional<AdminsMi> opAdm=adminsMiSer.getById(idadmin);
        if(opAdm==null){ throw new ExceptionCar("vous n'avez pas l'autorisation pour cette operation"); }
        AdminsMi adminsMi=opAdm.get();
        if(adminsMi==null){ throw new ExceptionCar("vous n'avez pas l'autorisation pour cette operation"); }

        Optional<AnnonceMi> opAn=getById(idannonce);
        if(opAn==null){ throw new ExceptionCar("annonce id:"+idannonce+" invalide"); }
        AnnonceMi annonceMi=opAn.get();
        if(annonceMi==null){ throw new ExceptionCar("annonce id:"+idannonce+" invalide"); }
        if(annonceMi.estEncours()==false){ throw new ExceptionCar("operation failed,annonce n'est plus encours de demande"); }
        else{ annonceMi.setEtatToRefus();; }
        AnnoncerefusMi annoncerefusMi=new AnnoncerefusMi(0, Date.valueOf(LocalDate.now()), idadmin, idannonce);
        annoncerefusMiSer.save(annoncerefusMi);
        save(annonceMi);//UPDATE ETAT
    }

    // public void enregistrer( MultipartFile file,String pathForSave)throws Exception{
    //     // Vérifiez si le fichier est vide
    //     if (file.isEmpty()) {
    //        throw new ExceptionCar("Le fichier est vide.");
    //     }
    //     try {
    //         // Obtenez le chemin du répertoire où vous souhaitez enregistrer le fichier
    //         //Path imagePath = Paths.get(pathForSave);

            // Path pfilePath = Paths.get(pathForSave);

    //         String uploadDir =pathForSave;
    //         String fileName = file.getOriginalFilename();
    //         String filePath = uploadDir + File.separator + fileName;
    //         // Créez le fichier dans le répertoire local
    //         File dest = new File(filePath);
    //         file.transferTo(dest);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         throw new ExceptionCar("Erreur lors de l'enregistrement du fichier.");
    //     }catch(Exception e){
    //         throw e;
    //     }
    // }

    // public void enregistrer( MultipartFile file,String pathForSave)throws Exception{
    //     // Vérifiez si le fichier est vide
    //     if (file.isEmpty()) {
    //        throw new ExceptionCar("Le fichier est vide.");
    //     }
    //     try {
    //         String uploadDir =pathForSave;
    //         String fileName = file.getOriginalFilename();
    //         String filePath = uploadDir + File.separator + fileName;
    //         // Créez le fichier dans le répertoire local
    //         File dest = new File(filePath);
    //         file.transferTo(dest);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         throw new ExceptionCar("Erreur lors de l'enregistrement du fichier.");
    //     }catch(Exception e){
    //         throw e;
    //     }
    // }

    // public MultipartFile tabByteToMultipartFile(byte[] bytes,String fileName)throws Exception{
    //     CommonsMu
    //     ByteArrayResource resource=new ByteArrayResource(bytes);
    //     //MultipartFile multipartFile=new YourCust
    //     return null;
    // }


    // public byte[] compress(byte[] originalImageData,int taille) throws IOException {
    //     BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalImageData));
    //     // Créer un flux de sortie pour stocker l'image compressée
    //     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    //     while (outputStream.size() > taille * 1024) { // 150 Ko
    //         outputStream.reset();// Réinitialiser le flux de sortie
    //         ImageIO.write(originalImage, "jpeg", outputStream);// Écrire l'image compressée dans le flux de sortie : la il le compresse
    //     }
    //     // Convertir l'image compressée en tableau de bytes
    //     ImageIO.write(originalImage, "jpeg", outputStream);
    //     byte[] compressedImageData = outputStream.toByteArray();
    //     return compressedImageData;
    // }
    // public void compressPhotoAndSet()throws Exception{
    //     byte[] photobyte=Base64.getDecoder().decode(this.photo);
    //     long tailleEnBytes = photobyte.length;
    //     double tailleEnMo = (double) tailleEnBytes / (1024 * 1024);
    //     if(tailleEnMo>=20){ throw new ExceptionCar("taille du fichier ne dois pas depasser de 20 Mo"); }
    //     byte[] photobytecompress=compress(photobyte, 150);
    //     String photocompress=Base64.getEncoder().encodeToString(photobytecompress);
    //     setPhoto(photocompress);
    // }
// ---statusvente : 0 : vendu /10 : non vendu
// ---etat : 0:encour demande / 10 :accepter / 20: refuser




    // @Transactional(rollbackFor = { Exception.class, ExceptionCar.class })
    // public void insertAnnonce(Annoncesave annoncesave,MultipartFile[] files)throws Exception{
        
    //     AnnonceMi annonce=new AnnonceMi(0,annoncesave.getPrixvente(),annoncesave.getDescription(),10,0,annoncesave.getIdlieu(),annoncesave.getIdvoitureinfo(),Timestamp.valueOf(LocalDateTime.now()));
    //     if(files==null){ throw new ExceptionCar("pas de(s) photo(s) selectionné(s)"); }
    //     else if(files.length==0){ throw new ExceptionCar("pas de(s) photo(s) selectionné(s)"); }
    //     // AnnoncephotoMi annoncephotoMi=null;
    //     // annonce=annonceRepository.save(annonce);

    //     //compresser ces fichiers:
    //     ImageService imgservice=new ImageService();
    //     MultipartFile[] filescompress=imgservice.compressImage(files);
    //     for(int i=0;i<filescompress.length;i++){
    //         System.out.println("name = "+filescompress[i]+" | bytes:"+filescompress[i].getBytes());
    //     }
    //     // for(int i=0;i<files.length;i++){
    //     //     annoncephotoMi=new AnnoncephotoMi(0,files[i].getOriginalFilename(),annonce.getIdannonce());
    //     //     annoncephotoRepository.save(annoncephotoMi);
    //     // }
    // }

    @Transactional(rollbackFor = { Exception.class, ExceptionCar.class })
    public void insertAnnonce2(Annoncesave annoncesave)throws Exception{
        AnnonceMi annonce=new AnnonceMi(0,annoncesave.getPrixvente(),annoncesave.getDescription(),10,0,annoncesave.getIdlieu(),annoncesave.getIdvoitureinfo(),Timestamp.valueOf(LocalDateTime.now()));
        
        MultipartFile[] files=annoncesave.getPhotofiles();
        if(files==null){ throw new ExceptionCar("pas de(s) photo(s) selectionné(s)"); }
        else if(files.length==0){ throw new ExceptionCar("pas de(s) photo(s) selectionné(s)"); }
        AnnoncephotoMi annoncephotoMi=null;
        annonce=annonceRepository.save(annonce);

        //compresser ces fichiers:
        // for(int i=0;i<files.length;i++){
        //     System.out.println("name = "+files[i]+" | bytes:"+(files[i].getSize()/1024));
        // }
        ImageService imgservice=new ImageService();
        MultipartFile[] filescompress=imgservice.compressImage(files);
        String[] liens=imgservice.uploadAndGetLinksImage(filescompress);

        // for(int i=0;i<filescompress.length;i++){
        //     System.out.println("name = "+filescompress[i]+" | bytes:"+(filescompress[i].getSize()/1024));
        // }

        for(int i=0;i<liens.length;i++){
            annoncephotoMi=new AnnoncephotoMi(0,liens[i],annonce.getIdannonce());
            annoncephotoRepository.save(annoncephotoMi);
        }
    }
    @Transactional(rollbackFor = { Exception.class, ExceptionCar.class })
    public void changerstatusMyAnnonce( int iduser,int idannonce,String datevente)throws Exception{
        //00--->efa vonon ny fonction
        //verifier si l'annonce est a l'user 00
        //verifie si l'annonce et un annonce accepter et n'est pas encore vendu 00
        //verifie s'il a assez de solde pour qu'on puisse enlever la commision 00
        //-->en prend la commission dans son compte (update soldeuser(solde,dateupdate) + insert dans debitsoldeuser) 00
        //-->insert creditsoldesite + update soldesite(solde+dateupdate) 00
        //-->changer le status : update 00
        //---insert vendu
        List<AnnonceMi> la = annonceRepository.getDisponbleToVenduByIdannonceByIduser(iduser, idannonce);
        AnnonceMi annonceMi = null;
        if( la.isEmpty() == true ){ throw new ExceptionCar("changement de status non autorisé,\n raison:annonce non existant pour l'user ou annonce n'est plus encours de demande ou user non existant"); }
        else { annonceMi = la.get(0); }
        SoldeuserMi soldeuserMi = soldeuserMiSer.getSoldeByIduser( iduser );
        if(soldeuserMi==null){ throw new ExceptionCar("pas de compte de solde pour extraire la commission"); }
        double solde=soldeuserMi.getSolde();
        double taux=regletauxMiSer.getTauxCommision();
        double commision=(taux*annonceMi.getPrixvente())/100;
        System.out.println("Commission:"+commision+" / solde:"+solde+"");
        if(solde<commision){ throw new ExceptionCar("solde insuffisant pour soustraire la commission de valeur"+commision); }
        soldeuserMi.setSolde(solde-commision);

        
        LocalDateTime nowlt=LocalDateTime.now();
        soldeuserMi.setDateupdate(Timestamp.valueOf(nowlt));
        MotifMi motifMi=motifMiSer.getMotifCommission();
        if(motifMi==null){ throw new ExceptionCar("donnee lmotif de commission null"); }

        soldeuserMiRep.save(soldeuserMi);
        DebitersoldeuserMi debitersoldeuserMi=new DebitersoldeuserMi(0, commision,Timestamp.valueOf(nowlt) , motifMi.getIdmotif(), soldeuserMi.getIdsoldeuser());
        debitersoldeuserMi=debitersoldeuserMiRep.save(debitersoldeuserMi);

        SoldesiteMi soldesiteMi=soldesiteMiSer.getSoldesiteMi();
        if(soldesiteMi==null){ throw new ExceptionCar("le site n'as pas compte solde pour stocker le gain de commission"); }
        soldesiteMi.setSolde(soldesiteMi.getSolde()+commision);
        soldesiteMi.setDateupdate(Timestamp.valueOf(nowlt));
        soldesiteMi=soldesiteMiRep.save(soldesiteMi);//uodate
        CreditersoldesiteMi creditersoldesiteMi=new CreditersoldesiteMi(0, commision, Timestamp.valueOf(nowlt), debitersoldeuserMi.getIddebit(), motifMi.getIdmotif(),soldesiteMi.getIdsoldesite());
        creditersoldesiteMiRep.save(creditersoldesiteMi);
        annonceMi.setStatusvente(0);
        annonceRepository.save(annonceMi);

        VenduMi venduMi=new VenduMi(0, null,Date.valueOf(LocalDate.now()), annonceMi.getIdannonce());
        venduMi.setDatevente(datevente);
        venduMi=venduMiRep.save(venduMi);
    }

    public List<AnnonceMi> getAll() {
        return annonceRepository.findAll();
    }
    public AnnonceMi[] getTabAll(){
        List<AnnonceMi> lst=getAll();
        if(lst==null){ return null; }
        if(lst.isEmpty()==true){ return null; }
        AnnonceMi[] Annonces=new AnnonceMi[lst.size()];
        for(int i=0;i<lst.size();i++){
            Annonces[i]=lst.get(i);
        }
        return Annonces;
    }
    // Méthode pour récupérer un  par son ID
    public Optional<AnnonceMi> getById(int id) {
        return annonceRepository.findById(id);
    }
    // Méthode pour supprimer un  par son ID
    public void delete(int id) {
        annonceRepository.deleteById(id);
    }
}
