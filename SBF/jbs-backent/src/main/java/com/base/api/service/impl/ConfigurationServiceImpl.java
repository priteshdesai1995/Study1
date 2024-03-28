package com.base.api.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.entities.Configuration;
import com.base.api.entities.Logo;
import com.base.api.repository.ConfigurationRepository;
import com.base.api.repository.LogoRepository;
import com.base.api.service.ConfigurationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    ConfigurationRepository configurationRepository;

    @Autowired
    LogoRepository logoRepository;

    /**
     * Gets the configurations.
     *
     * @return the configurations
     */
    @Override
    public List<Configuration> getConfigurations() {

        List<Configuration> configurationEntities = configurationRepository.findAll();

        return configurationEntities;
    }

    /**
     * Gets the logo.
     *
     * @return the logo
     */
    @Override
    public Logo getLogo() {

        List<Logo> logoEntities = logoRepository.findAll();

        if (logoEntities.size() > 0) {
            return logoEntities.get(0);
        }
        return new Logo();
    }

    /**
     * Gets the logo.
     *
     * @param siteName the site name
     * @return the logo
     */
    @Override
    public List<Logo> getSiteLogo(String siteName) {

        List<Logo> logoEntities = logoRepository.findBysiteName(siteName);

        for (Logo logo : logoEntities) {
            logo.setLogo(null);
            logo.setFavicon(null);
            logoRepository.save(logo);
        }

        return logoRepository.findBysiteName(siteName);
    }

    /**
     * Update configuration.
     *
     * @param request the request
     * @return the string
     */
    @SuppressWarnings("null")
    @Override
    public String updateConfiguration(Map<String, String> request) {

        String type = "text";
        String optionType = "general";

        try {
            String siteName = null;
            String contactNumber = null;
            
            for (Map.Entry<String, String> entry : request.entrySet()) {
                Configuration configurationEntity = configurationRepository.findByOptionName(entry.getKey());
                if (configurationEntity != null) {
                    configurationEntity.setOptionValue(entry.getValue().toString());

                } else {
                    configurationEntity = new Configuration();

                    configurationEntity.setOptionName(entry.getKey());
                    configurationEntity.setOptionValue(entry.getValue().toString());

                    if (entry.getKey().equals("site_name")) {
                        siteName = entry.getValue();
                    }

                    if (entry.getKey().equals("contact_number")) {
                        contactNumber = entry.getValue();
                    }

                    if (entry.getKey().equals("logo")) {
                        type = "logo";
                    } else if (entry.getKey().equals("favicon")) {
                        type = "favicon";
                    }

                    if (entry.getKey().contains("version")) {
                        optionType = "1.0";
                    }

                    configurationEntity.setType(type);
                    configurationEntity.setOptionType(optionType);

                }
                configurationRepository.save(configurationEntity);

                // set icon and logo
//				Logo logo = new Logo();
//				logo.setSiteName(siteName);
//				logo.setLogo(compressBytes(icon.getBytes()));
//				logo.setFavicon(compressBytes(icon.getBytes()));
//				logoRepository.save(logo);
            }
            return HttpStatus.OK.name();
        } catch (Exception e) {
            log.error(e.getMessage());
            return HttpStatus.BAD_REQUEST.name();
        }
    }

    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
