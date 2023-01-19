package com.yong2ss.pass.repository.packaze;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest - 보통 Repository Test

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class PackageRepositoryTest {

    @Autowired
    private PackageRepository packageRepository;

    @Test
    public void test_save() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("기본 PT 12주");
        packageEntity.setPeriod(48);;

        //when
        packageRepository.save(packageEntity);

        //then
        assertNotNull(packageEntity.getPackageSeq());
    }

    @Test
    public void test_findByCreatedAtAfter() {
        //given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        PackageEntity packageEntity1 = new PackageEntity();
        packageEntity1.setPackageName("기본 PT 3개월");
        packageEntity1.setPeriod(90);
        packageRepository.save(packageEntity1);

        PackageEntity packageEntity2 = new PackageEntity();
        packageEntity2.setPackageName("기본 PT 6개월");
        packageEntity2.setPeriod(180);
        packageRepository.save(packageEntity2);

        //when
        final List<PackageEntity> packageEntities = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0, 1, Sort.by("packageSeq").descending()));

        //then
        assertEquals(1, packageEntities.size());
        assertEquals(packageEntities.get(0).getPackageSeq(), packageEntity2.getPackageSeq());

    }

    @Test
    public void test_updateCountAndPeriod() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("기본 PT 이벤트 4개월");
        packageEntity.setPeriod(90);
        packageRepository.save(packageEntity);

        //when
        int updatedCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        final PackageEntity entity = packageRepository.findById(packageEntity.getPackageSeq()).get();

        //then
        assertEquals(1, updatedCount);
        assertEquals(30, entity.getCount());
        assertEquals(120, entity.getPeriod());
    }

    @Test
    public void test_Delete() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("삭제 예정");
        packageEntity.setCount(1);
        packageRepository.save(packageEntity);

        //when
        packageRepository.deleteById(packageEntity.getPackageSeq());

        //then
        assertTrue(packageRepository.findById(packageEntity.getPackageSeq()).isEmpty());
    }

}