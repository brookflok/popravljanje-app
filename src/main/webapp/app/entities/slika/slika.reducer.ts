import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISlika, defaultValue } from 'app/shared/model/slika.model';

export const ACTION_TYPES = {
  SEARCH_SLIKAS: 'slika/SEARCH_SLIKAS',
  FETCH_SLIKA_LIST: 'slika/FETCH_SLIKA_LIST',
  FETCH_SLIKA: 'slika/FETCH_SLIKA',
  CREATE_SLIKA: 'slika/CREATE_SLIKA',
  UPDATE_SLIKA: 'slika/UPDATE_SLIKA',
  DELETE_SLIKA: 'slika/DELETE_SLIKA',
  SET_BLOB: 'slika/SET_BLOB',
  RESET: 'slika/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISlika>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type SlikaState = Readonly<typeof initialState>;

// Reducer

export default (state: SlikaState = initialState, action): SlikaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SLIKAS):
    case REQUEST(ACTION_TYPES.FETCH_SLIKA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SLIKA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SLIKA):
    case REQUEST(ACTION_TYPES.UPDATE_SLIKA):
    case REQUEST(ACTION_TYPES.DELETE_SLIKA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_SLIKAS):
    case FAILURE(ACTION_TYPES.FETCH_SLIKA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SLIKA):
    case FAILURE(ACTION_TYPES.CREATE_SLIKA):
    case FAILURE(ACTION_TYPES.UPDATE_SLIKA):
    case FAILURE(ACTION_TYPES.DELETE_SLIKA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SLIKAS):
    case SUCCESS(ACTION_TYPES.FETCH_SLIKA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SLIKA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SLIKA):
    case SUCCESS(ACTION_TYPES.UPDATE_SLIKA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SLIKA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType,
        },
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/slikas';
const apiSearchUrl = 'api/_search/slikas';

// Actions

export const getSearchEntities: ICrudSearchAction<ISlika> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SLIKAS,
  payload: axios.get<ISlika>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<ISlika> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SLIKA_LIST,
  payload: axios.get<ISlika>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<ISlika> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SLIKA,
    payload: axios.get<ISlika>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISlika> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SLIKA,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISlika> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SLIKA,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISlika> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SLIKA,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType,
  },
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
