package com.weave.weave.domain.usecase

import com.weave.weave.data.repositoryImpl.UnivRepositoryImpl
import com.weave.weave.domain.entity.login.MajorEntity
import com.weave.weave.domain.entity.login.UniversityEntity
import com.weave.weave.domain.extension.asDomain

class UnivUseCase {
    private val univRepositoryImpl = UnivRepositoryImpl()


    suspend fun getUnivList(): Resource<List<UniversityEntity>>{
        return try {
            val res = univRepositoryImpl.findAllUniv()

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    val temp = arrayListOf<UniversityEntity>()
                    val universities = data.universities.listIterator()
                    while(universities.hasNext()){
                        temp.add(universities.next().asDomain())
                    }

                    Resource.Success(temp)
                } else {
                    Resource.Error("Received null data")
                }
            } else {
                Resource.Error(res.message())
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun getMajorList(univId: String): Resource<List<MajorEntity>>{
        return try {
            val res = univRepositoryImpl.getAllMajor(univId)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    val temp = arrayListOf<MajorEntity>()
                    val majors = data.majors.listIterator()
                    while(majors.hasNext()){
                        temp.add(majors.next().asDomain())
                    }

                    Resource.Success(temp)
                } else {
                    Resource.Error("Received null data")
                }
            } else {
                Resource.Error(res.message())
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}