import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMainSlika } from 'app/shared/model/main-slika.model';
import { getEntities as getMainSlikas } from 'app/entities/main-slika/main-slika.reducer';
import { IProfilnaSlika } from 'app/shared/model/profilna-slika.model';
import { getEntities as getProfilnaSlikas } from 'app/entities/profilna-slika/profilna-slika.reducer';
import { IGalerija } from 'app/shared/model/galerija.model';
import { getEntities as getGalerijas } from 'app/entities/galerija/galerija.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './slika.reducer';
import { ISlika } from 'app/shared/model/slika.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISlikaUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SlikaUpdate = (props: ISlikaUpdateProps) => {
  const [mainslikaId, setMainslikaId] = useState('0');
  const [mainslikaId, setMainslikaId] = useState('0');
  const [galerijaId, setGalerijaId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { slikaEntity, mainSlikas, profilnaSlikas, galerijas, loading, updating } = props;

  const { slika, slikaContentType } = slikaEntity;

  const handleClose = () => {
    props.history.push('/slika');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getMainSlikas();
    props.getProfilnaSlikas();
    props.getGalerijas();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.uploaded = convertDateTimeToServer(values.uploaded);

    if (errors.length === 0) {
      const entity = {
        ...slikaEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="popravljanjeApp.slika.home.createOrEditLabel">
            <Translate contentKey="popravljanjeApp.slika.home.createOrEditLabel">Create or edit a Slika</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : slikaEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="slika-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="slika-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="imeLabel" for="slika-ime">
                  <Translate contentKey="popravljanjeApp.slika.ime">Ime</Translate>
                </Label>
                <AvField id="slika-ime" type="text" name="ime" />
              </AvGroup>
              <AvGroup>
                <AvGroup>
                  <Label id="slikaLabel" for="slika">
                    <Translate contentKey="popravljanjeApp.slika.slika">Slika</Translate>
                  </Label>
                  <br />
                  {slika ? (
                    <div>
                      {slikaContentType ? (
                        <a onClick={openFile(slikaContentType, slika)}>
                          <img src={`data:${slikaContentType};base64,${slika}`} style={{ maxHeight: '100px' }} />
                        </a>
                      ) : null}
                      <br />
                      <Row>
                        <Col md="11">
                          <span>
                            {slikaContentType}, {byteSize(slika)}
                          </span>
                        </Col>
                        <Col md="1">
                          <Button color="danger" onClick={clearBlob('slika')}>
                            <FontAwesomeIcon icon="times-circle" />
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  ) : null}
                  <input id="file_slika" type="file" onChange={onBlobChange(true, 'slika')} accept="image/*" />
                  <AvInput type="hidden" name="slika" value={slika} />
                </AvGroup>
              </AvGroup>
              <AvGroup>
                <Label id="uploadedLabel" for="slika-uploaded">
                  <Translate contentKey="popravljanjeApp.slika.uploaded">Uploaded</Translate>
                </Label>
                <AvInput
                  id="slika-uploaded"
                  type="datetime-local"
                  className="form-control"
                  name="uploaded"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.slikaEntity.uploaded)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="slika-mainslika">
                  <Translate contentKey="popravljanjeApp.slika.mainslika">Mainslika</Translate>
                </Label>
                <AvInput id="slika-mainslika" type="select" className="form-control" name="mainslika.id">
                  <option value="" key="0" />
                  {mainSlikas
                    ? mainSlikas.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.ime}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="slika-mainslika">
                  <Translate contentKey="popravljanjeApp.slika.mainslika">Mainslika</Translate>
                </Label>
                <AvInput id="slika-mainslika" type="select" className="form-control" name="mainslika.id">
                  <option value="" key="0" />
                  {profilnaSlikas
                    ? profilnaSlikas.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.ime}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="slika-galerija">
                  <Translate contentKey="popravljanjeApp.slika.galerija">Galerija</Translate>
                </Label>
                <AvInput id="slika-galerija" type="select" className="form-control" name="galerija.id">
                  <option value="" key="0" />
                  {galerijas
                    ? galerijas.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.ime}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/slika" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  mainSlikas: storeState.mainSlika.entities,
  profilnaSlikas: storeState.profilnaSlika.entities,
  galerijas: storeState.galerija.entities,
  slikaEntity: storeState.slika.entity,
  loading: storeState.slika.loading,
  updating: storeState.slika.updating,
  updateSuccess: storeState.slika.updateSuccess,
});

const mapDispatchToProps = {
  getMainSlikas,
  getProfilnaSlikas,
  getGalerijas,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SlikaUpdate);
