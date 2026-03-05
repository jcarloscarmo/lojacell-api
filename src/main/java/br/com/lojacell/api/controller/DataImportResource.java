package br.com.lojacell.api.controller;

import br.com.lojacell.domain.model.Brand;
import br.com.lojacell.domain.model.DeviceModel;
import br.com.lojacell.domain.repository.BrandRepository;
import br.com.lojacell.domain.repository.DeviceModelRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/import-fipe")
@RequiredArgsConstructor
public class DataImportResource {

    private final BrandRepository brandRepository;
    private final DeviceModelRepository deviceModelRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    // URL oficial da v2 conforme a documentação
    private final String API_BASE_URL = "https://phone-specs-api-2.azharimm.dev/v2";

    @PostMapping
    @Transactional
    public String startImport() {
        try {
            // 1. Lista as Marcas
            BrandApiResponse brandResponse = restTemplate.getForObject(API_BASE_URL + "/brands", BrandApiResponse.class);

            if (brandResponse == null || brandResponse.getData() == null) return "API fora do ar";

            // Para não pesar seu banco, vamos focar nas marcas que mais vendem na sua região
            List<String> principaisMarcas = List.of("Apple", "Samsung", "Motorola", "Xiaomi", "LG");

            for (BrandData brandData : brandResponse.getData()) {
                if (!principaisMarcas.contains(brandData.brandName)) continue;

                // Salva a Marca e recupera para o vínculo
                Brand brand = brandRepository.findByName(brandData.brandName)
                        .orElseGet(() -> brandRepository.save(new Brand(brandData.brandName)));

                // 2. Busca modelos usando o Slug da marca (ex: apple-phones-48)
                String modelsUrl = API_BASE_URL + "/brands/" + brandData.brandSlug;
                ModelApiResponse modelResponse = restTemplate.getForObject(modelsUrl, ModelApiResponse.class);

                if (modelResponse != null && modelResponse.getData() != null && modelResponse.getData().getPhones() != null) {
                    for (PhoneData phoneData : modelResponse.getData().getPhones()) {
                        // Salva se o modelo não existir
                        if (!deviceModelRepository.existsByNameAndBrand(phoneData.phoneName, brand)) {
                            deviceModelRepository.save(new DeviceModel(phoneData.phoneName, brand));
                        }
                    }
                }
            }
            return "Importação concluída! Suas lojas JCELL agora têm uma base de dados profissional.";
        } catch (Exception e) {
            return "Erro na importação: " + e.getMessage();
        }
    }

    // --- DTOs AJUSTADOS PARA A V2 ---
    @Data static class BrandApiResponse { private List<BrandData> data; }
    @Data static class BrandData {
        @JsonProperty("brand_name") String brandName;
        @JsonProperty("brand_slug") String brandSlug;
    }

    @Data static class ModelApiResponse { private ModelData data; }
    @Data static class ModelData { private List<PhoneData> phones; } // Mudou de 'devices' para 'phones'
    @Data static class PhoneData {
        @JsonProperty("phone_name") String phoneName;
    }
}