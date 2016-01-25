package fr.pinguet62.jsfring.ws.converter;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mysema.query.types.Predicate;

import fr.pinguet62.jsfring.model.sql.Profile;
import fr.pinguet62.jsfring.model.sql.QRight;
import fr.pinguet62.jsfring.model.sql.Right;
import fr.pinguet62.jsfring.service.RightService;
import fr.pinguet62.jsfring.ws.dto.ProfileDto;

@Component
public final class ProfileFromDtoConverter implements Converter<ProfileDto, Profile> {

    @Inject
    private RightService rightService;

    @Override
    public Profile convert(ProfileDto dto) {
        Profile profile = new Profile();

        profile.setId(dto.getId());
        profile.setTitle(dto.getTitle());

        Predicate predicate = QRight.right_.code.in(dto.getRights());
        List<Right> rights = rightService.findAll(predicate);
        profile.setRights(new HashSet<>(rights));

        return profile;
    }

    @Override
    public String toString() {
        return ProfileDto.class.getName() + " -> " + Profile.class.getName() + " : " + super.toString();
    }

}