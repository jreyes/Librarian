package librarian.service;

import librarian.domain.Setting;
import librarian.repository.SettingRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AdminService {
// ------------------------------ FIELDS ------------------------------

    private final SettingRepository settingRepository;

// --------------------------- CONSTRUCTORS ---------------------------

    public AdminService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

// -------------------------- OTHER METHODS --------------------------

    @Transactional(readOnly = true)
    public boolean authenticate(String username, String password) {
        return settingRepository.findById(username)
                .map(setting -> setting.getPassword().equals(DigestUtils.sha1Hex(password)))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public Optional<Setting> getSettings() {
        return settingRepository.findAll().stream().findFirst();
    }

    public void updateSettings(String username,
                               String password,
                               float finePerDay,
                               int daysWithoutFine,
                               String apiKey) {
        getSettings().ifPresent(setting -> {
            setting.setUsername(username);
            if (!setting.getPassword().equals(password)) {
                setting.setPassword(DigestUtils.sha1Hex(password));
            }
            setting.setFinePerDay(finePerDay);
            setting.setDaysWithoutFine(daysWithoutFine);
            setting.setApiKey(apiKey);
            settingRepository.save(setting);
        });
    }
}
