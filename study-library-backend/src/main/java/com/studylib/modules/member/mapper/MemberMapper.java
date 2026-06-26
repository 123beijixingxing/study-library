package com.studylib.modules.member.mapper;

import com.studylib.modules.member.entity.MemberEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberMapper {

  MemberEntity selectById(@Param("id") Long id);

  List<MemberEntity> selectList(@Param("keyword") String keyword, @Param("status") String status);

  Long countPage(@Param("keyword") String keyword, @Param("status") String status);

  List<MemberEntity> selectPage(@Param("keyword") String keyword, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer update(MemberEntity entity);
}
