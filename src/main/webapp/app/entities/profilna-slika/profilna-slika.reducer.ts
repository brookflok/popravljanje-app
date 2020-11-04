import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProfilnaSlika, defaultValue } from 'app/shared/model/profilna-slika.model';

export const ACTION_TYPES = {
  SEARCH_PROFILNASLIKAS: 'profilnaSlika/SEARCH_PROFILNASLIKAS',
  FETCH_PROFILNASLIKA_LIST: 'profilnaSlika/FETCH_PROFILNASLIKA_LIST',
  FETCH_PROFILNASLIKA: 'profilnaSlika/FETCH_PROFILNASLIKA',
  CREATE_PROFILNASLIKA: 'profilnaSlika/CREATE_PROFILNASLIKA',
  UPDATE_PROFILNASLIKA: 'profilnaSlika/UPDATE_PROFILNASLIKA',
  DELETE_PROFILNASLIKA: 'profilnaSlika/DELETE_PROFILNASLIKA',
  RESET: 'profilnaSlika/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProfilnaSlika>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ProfilnaSlikaState = Readonly<typeof initialState>;

// Reducer

export default (state: ProfilnaSlikaState = initialState, action): ProfilnaSlikaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PROFILNASLIKAS):
    case REQUEST(ACTION_TYPES.FETCH_PROFILNASLIKA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PROFILNASLIKA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PROFILNASLIKA):
    case REQUEST(ACTION_TYPES.UPDATE_PROFILNASLIKA):
    case REQUEST(ACTION_TYPES.DELETE_PROFILNASLIKA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_PROFILNASLIKAS):
    case FAILURE(ACTION_TYPES.FETCH_PROFILNASLIKA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PROFILNASLIKA):
    case FAILURE(ACTION_TYPES.CREATE_PROFILNASLIKA):
    case FAILURE(ACTION_TYPES.UPDATE_PROFILNASLIKA):
    case FAILURE(ACTION_TYPES.DELETE_PROFILNASLIKA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PROFILNASLIKAS):
    case SUCCESS(ACTION_TYPES.FETCH_PROFILNASLIKA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROFILNASLIKA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PROFILNASLIKA):
    case SUCCESS(ACTION_TYPES.UPDATE_PROFILNASLIKA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PROFILNASLIKA):
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

const apiUrl = 'api/profilna-slikas';
const apiSearchUrl = 'api/_search/profilna-slikas';

// Actions

export const getSearchEntities: ICrudSearchAction<IProfilnaSlika> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_PROFILNASLIKAS,
  payload: axios.get<IProfilnaSlika>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IProfilnaSlika> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PROFILNASLIKA_LIST,
  payload: axios.get<IProfilnaSlika>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IProfilnaSlika> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PROFILNASLIKA,
    payload: axios.get<IProfilnaSlika>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProfilnaSlika> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PROFILNASLIKA,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProfilnaSlika> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PROFILNASLIKA,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProfilnaSlika> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PROFILNASLIKA,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
