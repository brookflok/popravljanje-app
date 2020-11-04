import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEntiteti, defaultValue } from 'app/shared/model/entiteti.model';

export const ACTION_TYPES = {
  SEARCH_ENTITETIS: 'entiteti/SEARCH_ENTITETIS',
  FETCH_ENTITETI_LIST: 'entiteti/FETCH_ENTITETI_LIST',
  FETCH_ENTITETI: 'entiteti/FETCH_ENTITETI',
  CREATE_ENTITETI: 'entiteti/CREATE_ENTITETI',
  UPDATE_ENTITETI: 'entiteti/UPDATE_ENTITETI',
  DELETE_ENTITETI: 'entiteti/DELETE_ENTITETI',
  RESET: 'entiteti/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEntiteti>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type EntitetiState = Readonly<typeof initialState>;

// Reducer

export default (state: EntitetiState = initialState, action): EntitetiState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ENTITETIS):
    case REQUEST(ACTION_TYPES.FETCH_ENTITETI_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ENTITETI):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ENTITETI):
    case REQUEST(ACTION_TYPES.UPDATE_ENTITETI):
    case REQUEST(ACTION_TYPES.DELETE_ENTITETI):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ENTITETIS):
    case FAILURE(ACTION_TYPES.FETCH_ENTITETI_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ENTITETI):
    case FAILURE(ACTION_TYPES.CREATE_ENTITETI):
    case FAILURE(ACTION_TYPES.UPDATE_ENTITETI):
    case FAILURE(ACTION_TYPES.DELETE_ENTITETI):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ENTITETIS):
    case SUCCESS(ACTION_TYPES.FETCH_ENTITETI_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ENTITETI):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ENTITETI):
    case SUCCESS(ACTION_TYPES.UPDATE_ENTITETI):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ENTITETI):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/entitetis';
const apiSearchUrl = 'api/_search/entitetis';

// Actions

export const getSearchEntities: ICrudSearchAction<IEntiteti> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ENTITETIS,
  payload: axios.get<IEntiteti>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IEntiteti> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ENTITETI_LIST,
  payload: axios.get<IEntiteti>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IEntiteti> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ENTITETI,
    payload: axios.get<IEntiteti>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IEntiteti> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ENTITETI,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEntiteti> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ENTITETI,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEntiteti> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ENTITETI,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
