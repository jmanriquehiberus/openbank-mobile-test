package com.javiermp.datasources.remote.mapper

/**
 * Interface for model mappers. It provides helper methods that facilitate
 * retrieving of models from outer data source layers
 *
 * @param <M> the model output type
 * @param <R> the remote response model input type
 */
interface RemoteMapper<M,R> {

    fun mapFromRemote(remote: R): M
}