package com.unicamp.dslearn.domain.cardsearch

import com.unicamp.dslearn.data.repository.SearchRepository

class SearchCardUseCaseImpl(private val searchRepository: SearchRepository) : SearchCardUseCase {

    override fun invoke(query: String) = searchRepository.searchByQuery(query)
}