import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILokacija } from 'app/shared/model/lokacija.model';
import { getEntities as getLokacijas } from 'app/entities/lokacija/lokacija.reducer';
import { IProfilnaSlika } from 'app/shared/model/profilna-slika.model';
import { getEntities as getProfilnaSlikas } from 'app/entities/profilna-slika/profilna-slika.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IPoruka } from 'app/shared/model/poruka.model';
import { getEntities as getPorukas } from 'app/entities/poruka/poruka.reducer';
import { IUcesnici } from 'app/shared/model/ucesnici.model';
import { getEntities as getUcesnicis } from 'app/entities/ucesnici/ucesnici.reducer';
import { getEntity, updateEntity, createEntity, reset } from './dodatni-info-user.reducer';
import { IDodatniInfoUser } from 'app/shared/model/dodatni-info-user.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDodatniInfoUserUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DodatniInfoUserUpdate = (props: IDodatniInfoUserUpdateProps) => {
  const [lokacijaId, setLokacijaId] = useState('0');
  const [profilnaSlikaId, setProfilnaSlikaId] = useState('0');
  const [userId, setUserId] = useState('0');
  const [porukaId, setPorukaId] = useState('0');
  const [ucesniciId, setUcesniciId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { dodatniInfoUserEntity, lokacijas, profilnaSlikas, users, porukas, ucesnicis, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/dodatni-info-user');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getLokacijas();
    props.getProfilnaSlikas();
    props.getUsers();
    props.getPorukas();
    props.getUcesnicis();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...dodatniInfoUserEntity,
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
          <h2 id="popravljanjeApp.dodatniInfoUser.home.createOrEditLabel">
            <Translate contentKey="popravljanjeApp.dodatniInfoUser.home.createOrEditLabel">Create or edit a DodatniInfoUser</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : dodatniInfoUserEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="dodatni-info-user-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="dodatni-info-user-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="korisnickoimeLabel" for="dodatni-info-user-korisnickoime">
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.korisnickoime">Korisnickoime</Translate>
                </Label>
                <AvField id="dodatni-info-user-korisnickoime" type="text" name="korisnickoime" />
              </AvGroup>
              <AvGroup>
                <Label id="brojTelefonaLabel" for="dodatni-info-user-brojTelefona">
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.brojTelefona">Broj Telefona</Translate>
                </Label>
                <AvField id="dodatni-info-user-brojTelefona" type="text" name="brojTelefona" />
              </AvGroup>
              <AvGroup check>
                <Label id="majstorLabel">
                  <AvInput id="dodatni-info-user-majstor" type="checkbox" className="form-check-input" name="majstor" />
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.majstor">Majstor</Translate>
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="postojiLabel">
                  <AvInput id="dodatni-info-user-postoji" type="checkbox" className="form-check-input" name="postoji" />
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.postoji">Postoji</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="detaljniOpisLabel" for="dodatni-info-user-detaljniOpis">
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.detaljniOpis">Detaljni Opis</Translate>
                </Label>
                <AvField id="dodatni-info-user-detaljniOpis" type="text" name="detaljniOpis" />
              </AvGroup>
              <AvGroup>
                <Label for="dodatni-info-user-lokacija">
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.lokacija">Lokacija</Translate>
                </Label>
                <AvInput id="dodatni-info-user-lokacija" type="select" className="form-control" name="lokacija.id">
                  <option value="" key="0" />
                  {lokacijas
                    ? lokacijas.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="dodatni-info-user-profilnaSlika">
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.profilnaSlika">Profilna Slika</Translate>
                </Label>
                <AvInput id="dodatni-info-user-profilnaSlika" type="select" className="form-control" name="profilnaSlika.id">
                  <option value="" key="0" />
                  {profilnaSlikas
                    ? profilnaSlikas.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="dodatni-info-user-user">
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.user">User</Translate>
                </Label>
                <AvInput id="dodatni-info-user-user" type="select" className="form-control" name="user.id">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="dodatni-info-user-ucesnici">
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.ucesnici">Ucesnici</Translate>
                </Label>
                <AvInput id="dodatni-info-user-ucesnici" type="select" className="form-control" name="ucesnici.id">
                  <option value="" key="0" />
                  {ucesnicis
                    ? ucesnicis.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/dodatni-info-user" replace color="info">
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
  lokacijas: storeState.lokacija.entities,
  profilnaSlikas: storeState.profilnaSlika.entities,
  users: storeState.userManagement.users,
  porukas: storeState.poruka.entities,
  ucesnicis: storeState.ucesnici.entities,
  dodatniInfoUserEntity: storeState.dodatniInfoUser.entity,
  loading: storeState.dodatniInfoUser.loading,
  updating: storeState.dodatniInfoUser.updating,
  updateSuccess: storeState.dodatniInfoUser.updateSuccess,
});

const mapDispatchToProps = {
  getLokacijas,
  getProfilnaSlikas,
  getUsers,
  getPorukas,
  getUcesnicis,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DodatniInfoUserUpdate);
