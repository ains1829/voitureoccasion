package com.dev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.body.InfoCar;
import com.dev.exception.ExceptionCar;
import com.dev.models.CategorievoitureMi;
import com.dev.models.VoitureinfoMi;
import com.dev.repository.CategorievoitureMiRep;
import com.dev.repository.ModelcategorieMiPersoRep;
import com.dev.repository.VoitureinfoMiRep;

import java.util.List;
import java.util.Optional;

@Service
public class VoitureinfoMiSer {

    @Autowired
    private VoitureinfoMiRep voitureinfoRepository;
    @Autowired
    private CategorievoitureMiRep categorievoitureMiRep;
    @Autowired
    private ModelcategorieMiPersoRep modelcategorieMiPersoRep;

    public VoitureinfoMi save(VoitureinfoMi Article) {
        return voitureinfoRepository.save(Article);
    }

    //VoitureinfoMi(int idvoitureinfo,String nomvoiture,int nombreplace,double kilometrage,int transmission,double vitesse,int iduser,int idcarburant,int idmarque,int idmodel)
    @Transactional(rollbackFor = { Exception.class, ExceptionCar.class })
    public void saveByInfoCar(InfoCar infoCar)throws Exception {
            VoitureinfoMi voitureinfoMi=new VoitureinfoMi(0, infoCar.getNomvoiture(), infoCar.getNbplace(), infoCar.getKilometrage(), infoCar.getIduser(), infoCar.getIdmodel());
            voitureinfoMi=voitureinfoRepository.save(voitureinfoMi);
    }

    // Méthode pour récupérer tous les Articles
    public List<VoitureinfoMi> getAll() {
        return voitureinfoRepository.findAll();
    }
    public VoitureinfoMi[] getTabAll(){
        List<VoitureinfoMi> lstA=getAll();
        if(lstA==null){ return null; }
        if(lstA.isEmpty()==true){ return null; }
        VoitureinfoMi[] articles=new VoitureinfoMi[lstA.size()];
        for(int i=0;i<lstA.size();i++){
            articles[i]=lstA.get(i);
        }
        return articles;
    }
    // Méthode pour récupérer un Article par son ID
    public Optional<VoitureinfoMi> getById(int id) {
        return voitureinfoRepository.findById(id);
    }
    // Méthode pour supprimer un Article par son ID
    public void delete(int id) {
        voitureinfoRepository.deleteById(id);
    }
}
