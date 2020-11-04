import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudSearchAction, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './dodatni-info-user.reducer';
import { IDodatniInfoUser } from 'app/shared/model/dodatni-info-user.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDodatniInfoUserProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const DodatniInfoUser = (props: IDodatniInfoUserProps) => {
  const [search, setSearch] = useState('');

  useEffect(() => {
    props.getEntities();
  }, []);

  const startSearching = () => {
    if (search) {
      props.getSearchEntities(search);
    }
  };

  const clear = () => {
    setSearch('');
    props.getEntities();
  };

  const handleSearch = event => setSearch(event.target.value);

  const { dodatniInfoUserList, match, loading } = props;
  return (
    <div>
      <h2 id="dodatni-info-user-heading">
        <Translate contentKey="popravljanjeApp.dodatniInfoUser.home.title">Dodatni Info Users</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="popravljanjeApp.dodatniInfoUser.home.createLabel">Create new Dodatni Info User</Translate>
        </Link>
      </h2>
      <Row>
        <Col sm="12">
          <AvForm onSubmit={startSearching}>
            <AvGroup>
              <InputGroup>
                <AvInput
                  type="text"
                  name="search"
                  value={search}
                  onChange={handleSearch}
                  placeholder={translate('popravljanjeApp.dodatniInfoUser.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </AvGroup>
          </AvForm>
        </Col>
      </Row>
      <div className="table-responsive">
        {dodatniInfoUserList && dodatniInfoUserList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.korisnickoime">Korisnickoime</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.brojTelefona">Broj Telefona</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.majstor">Majstor</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.postoji">Postoji</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.detaljniOpis">Detaljni Opis</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.lokacija">Lokacija</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.profilnaSlika">Profilna Slika</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="popravljanjeApp.dodatniInfoUser.ucesnici">Ucesnici</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {dodatniInfoUserList.map((dodatniInfoUser, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${dodatniInfoUser.id}`} color="link" size="sm">
                      {dodatniInfoUser.id}
                    </Button>
                  </td>
                  <td>{dodatniInfoUser.korisnickoime}</td>
                  <td>{dodatniInfoUser.brojTelefona}</td>
                  <td>{dodatniInfoUser.majstor ? 'true' : 'false'}</td>
                  <td>{dodatniInfoUser.postoji ? 'true' : 'false'}</td>
                  <td>{dodatniInfoUser.detaljniOpis}</td>
                  <td>
                    {dodatniInfoUser.lokacija ? (
                      <Link to={`lokacija/${dodatniInfoUser.lokacija.id}`}>{dodatniInfoUser.lokacija.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {dodatniInfoUser.profilnaSlika ? (
                      <Link to={`profilna-slika/${dodatniInfoUser.profilnaSlika.id}`}>{dodatniInfoUser.profilnaSlika.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{dodatniInfoUser.user ? dodatniInfoUser.user.id : ''}</td>
                  <td>
                    {dodatniInfoUser.ucesnici ? (
                      <Link to={`ucesnici/${dodatniInfoUser.ucesnici.id}`}>{dodatniInfoUser.ucesnici.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${dodatniInfoUser.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${dodatniInfoUser.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${dodatniInfoUser.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="popravljanjeApp.dodatniInfoUser.home.notFound">No Dodatni Info Users found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ dodatniInfoUser }: IRootState) => ({
  dodatniInfoUserList: dodatniInfoUser.entities,
  loading: dodatniInfoUser.loading,
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DodatniInfoUser);
